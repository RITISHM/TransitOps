import os
import re

def process_file(filepath):
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()

    if 'lombok' not in content and '@Getter' not in content and '@RequiredArgsConstructor' not in content and '@Data' not in content:
        return

    print(f"Processing {filepath}")
    
    # Remove lombok imports
    lines = content.split('\n')
    lines = [line for line in lines if not line.strip().startswith('import lombok.')]
    
    # Remove annotations
    annotations_to_remove = ['@Getter', '@Setter', '@NoArgsConstructor', '@AllArgsConstructor', '@Builder', '@Data', '@RequiredArgsConstructor']
    cleaned_lines = []
    
    for line in lines:
        stripped = line.strip()
        if any(stripped == ann for ann in annotations_to_remove):
            continue
        # Also handle @Builder.Default (remove it)
        if stripped == '@Builder.Default':
            continue
        cleaned_lines.append(line)
        
    content = '\n'.join(cleaned_lines)
    
    # Find class name
    class_match = re.search(r'public class (\w+)', content)
    if not class_match:
        return
    class_name = class_match.group(1)
    
    # Find all fields
    # Match: private [final] Type name;
    # Or: private Type name = default;
    field_pattern = r'(?:private|protected)\s+(final\s+)?([\w<>\?, ]+)\s+(\w+)(?:\s*=\s*[^;]+)?;'
    fields = re.findall(field_pattern, content)
    
    # Generate methods
    methods = []
    
    # Required args constructor for @RequiredArgsConstructor
    if '@RequiredArgsConstructor' in open(filepath).read() or '@Data' in open(filepath).read():
        final_fields = [f for f in fields if f[0].strip() == 'final']
        if final_fields:
            args = ", ".join([f"{f[1]} {f[2]}" for f in final_fields])
            assigns = "\n".join([f"        this.{f[2]} = {f[2]};" for f in final_fields])
            methods.append(f"    public {class_name}({args}) {{\n{assigns}\n    }}")

    # No args
    if '@NoArgsConstructor' in open(filepath).read() or '@Data' in open(filepath).read() or '@Builder' in open(filepath).read():
        methods.append(f"    public {class_name}() {{\n    }}")
        
    # All args
    if '@AllArgsConstructor' in open(filepath).read() or '@Builder' in open(filepath).read():
        args = ", ".join([f"{f[1]} {f[2]}" for f in fields])
        assigns = "\n".join([f"        this.{f[2]} = {f[2]};" for f in fields])
        methods.append(f"    public {class_name}({args}) {{\n{assigns}\n    }}")
        
    # Getters and Setters
    if '@Getter' in open(filepath).read() or '@Data' in open(filepath).read():
        for _, type_str, name in fields:
            capitalized = name[0].upper() + name[1:]
            prefix = 'is' if type_str.strip() == 'boolean' or type_str.strip() == 'Boolean' else 'get'
            methods.append(f"    public {type_str} {prefix}{capitalized}() {{\n        return {name};\n    }}")

    if '@Setter' in open(filepath).read() or '@Data' in open(filepath).read():
        for is_final, type_str, name in fields:
            if is_final.strip() != 'final':
                capitalized = name[0].upper() + name[1:]
                methods.append(f"    public void set{capitalized}({type_str} {name}) {{\n        this.{name} = {name};\n    }}")
                
    # Builder
    if '@Builder' in open(filepath).read():
        methods.append(f"    public static {class_name}Builder builder() {{\n        return new {class_name}Builder();\n    }}")
        builder_class = [f"    public static class {class_name}Builder {{"]
        for _, type_str, name in fields:
            builder_class.append(f"        private {type_str} {name};")
        
        for _, type_str, name in fields:
            builder_class.append(f"        public {class_name}Builder {name}({type_str} {name}) {{\n            this.{name} = {name};\n            return this;\n        }}")
            
        build_args = ", ".join([f"this.{name}" for _, _, name in fields])
        builder_class.append(f"        public {class_name} build() {{\n            return new {class_name}({build_args});\n        }}")
        builder_class.append("    }")
        methods.append("\n".join(builder_class))
        
    # Insert methods before last closing brace
    if methods:
        last_brace_idx = content.rfind('}')
        if last_brace_idx != -1:
            methods_str = "\n\n" + "\n\n".join(methods) + "\n"
            content = content[:last_brace_idx] + methods_str + content[last_brace_idx:]
            
    with open(filepath, 'w', encoding='utf-8') as f:
        f.write(content)

def main():
    src_dir = os.path.join(os.path.dirname(__file__), 'src')
    for root, _, files in os.walk(src_dir):
        for file in files:
            if file.endswith('.java'):
                process_file(os.path.join(root, file))

if __name__ == '__main__':
    main()

import os
import xml.etree.ElementTree as ET

reports_dir = os.path.join(os.path.dirname(__file__), 'target', 'surefire-reports')
if not os.path.isdir(reports_dir):
    print('Surefire reports directory not found:', reports_dir)
    exit(1)

total_tests = total_errors = total_failures = total_skipped = 0
for filename in os.listdir(reports_dir):
    if not filename.startswith('TEST-') or not filename.endswith('.xml'):
        continue
    path = os.path.join(reports_dir, filename)
    try:
        tree = ET.parse(path)
        root = tree.getroot()
        # attributes on testsuite element
        total_tests += int(root.attrib.get('tests', 0))
        total_errors += int(root.attrib.get('errors', 0))
        total_failures += int(root.attrib.get('failures', 0))
        total_skipped += int(root.attrib.get('skipped', 0))
    except Exception as e:
        print(f'Error parsing {filename}: {e}')

print('Test Summary:')
print(f'  Total tests   : {total_tests}')
print(f'  Total errors   : {total_errors}')
print(f'  Total failures : {total_failures}')
print(f'  Total skipped  : {total_skipped}')

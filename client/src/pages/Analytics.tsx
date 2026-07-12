import React from 'react';

const Analytics = () => {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '32px' }}>
      
      {/* METRIC CARDS */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(4, 1fr)', gap: '24px' }}>
        <div className="panel" style={{ padding: '24px' }}>
          <div style={{ fontSize: '0.75rem', fontWeight: 600, color: 'var(--text-secondary)', textTransform: 'uppercase', marginBottom: '8px' }}>FUEL EFFICIENCY</div>
          <div style={{ fontSize: '1.75rem', fontWeight: 700 }}>8.4 km/l</div>
        </div>
        <div className="panel" style={{ padding: '24px' }}>
          <div style={{ fontSize: '0.75rem', fontWeight: 600, color: 'var(--text-secondary)', textTransform: 'uppercase', marginBottom: '8px' }}>FLEET UTILIZATION</div>
          <div style={{ fontSize: '1.75rem', fontWeight: 700 }}>89%</div>
        </div>
        <div className="panel" style={{ padding: '24px' }}>
          <div style={{ fontSize: '0.75rem', fontWeight: 600, color: 'var(--text-secondary)', textTransform: 'uppercase', marginBottom: '8px' }}>OPERATIONAL COST</div>
          <div style={{ fontSize: '1.75rem', fontWeight: 700 }}>34,070</div>
        </div>
        <div className="panel" style={{ padding: '24px' }}>
          <div style={{ fontSize: '0.75rem', fontWeight: 600, color: 'var(--text-secondary)', textTransform: 'uppercase', marginBottom: '8px' }}>VEHICLE ROI</div>
          <div style={{ fontSize: '1.75rem', fontWeight: 700 }}>14.2%</div>
        </div>
      </div>
      
      <div style={{ fontSize: '0.75rem', color: 'var(--text-secondary)', marginTop: '-24px', fontStyle: 'italic', paddingLeft: '8px' }}>
        ROI = (Revenue - (Maintenance + Fuel)) / Acquisition Cost
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: '3fr 2fr', gap: '32px' }}>
        
        {/* MONTHLY REVENUE */}
        <div className="panel" style={{ padding: '32px' }}>
          <h3 style={{ fontSize: '0.85rem', color: 'var(--text-secondary)', textTransform: 'uppercase', marginBottom: '32px' }}>MONTHLY REVENUE</h3>
          
          <div style={{ display: 'flex', alignItems: 'flex-end', gap: '12px', height: '220px', borderBottom: '1px solid var(--border-color)', paddingBottom: '8px' }}>
            {/* Mock bars for the chart */}
            {[40, 55, 50, 75, 70, 90, 85].map((height, i) => (
              <div key={i} style={{ 
                flex: 1, 
                height: `${height}%`, 
                backgroundColor: '#3b82f6', 
                borderTopLeftRadius: '4px', 
                borderTopRightRadius: '4px',
                opacity: 0.9,
                transition: 'opacity 0.2s',
                cursor: 'pointer'
              }} 
              onMouseEnter={(e) => e.currentTarget.style.opacity = '1'}
              onMouseLeave={(e) => e.currentTarget.style.opacity = '0.9'}
              />
            ))}
          </div>
          <div style={{ display: 'flex', justifyContent: 'space-between', marginTop: '16px', fontSize: '0.75rem', color: 'var(--text-secondary)', fontWeight: 500, padding: '0 4px' }}>
            <span>Jan</span>
            <span>Feb</span>
            <span>Mar</span>
            <span>Apr</span>
            <span>May</span>
            <span>Jun</span>
            <span>Jul</span>
          </div>
        </div>

        {/* TOP COSTLIEST VEHICLES */}
        <div className="panel" style={{ padding: '32px' }}>
          <h3 style={{ fontSize: '0.85rem', color: 'var(--text-secondary)', textTransform: 'uppercase', marginBottom: '32px' }}>TOP COSTLIEST VEHICLES</h3>
          
          <div style={{ display: 'flex', flexDirection: 'column', gap: '32px' }}>
            <div>
              <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '12px', fontSize: '0.85rem', fontWeight: 600 }}>
                <span>TRUCK-11</span>
                <span>$18,400</span>
              </div>
              <div style={{ width: '100%', height: '24px', backgroundColor: 'var(--input-bg)', borderRadius: '12px', overflow: 'hidden' }}>
                <div style={{ width: '85%', height: '100%', backgroundColor: '#ef4444' }}></div>
              </div>
            </div>

            <div>
              <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '12px', fontSize: '0.85rem', fontWeight: 600 }}>
                <span>MINI-03</span>
                <span>$6,200</span>
              </div>
              <div style={{ width: '100%', height: '24px', backgroundColor: 'var(--input-bg)', borderRadius: '12px', overflow: 'hidden' }}>
                <div style={{ width: '40%', height: '100%', backgroundColor: '#d97706' }}></div>
              </div>
            </div>

            <div>
              <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '12px', fontSize: '0.85rem', fontWeight: 600 }}>
                <span>VAN-05</span>
                <span>$3,150</span>
              </div>
              <div style={{ width: '100%', height: '24px', backgroundColor: 'var(--input-bg)', borderRadius: '12px', overflow: 'hidden' }}>
                <div style={{ width: '20%', height: '100%', backgroundColor: '#3b82f6' }}></div>
              </div>
            </div>
          </div>

        </div>
      </div>

    </div>
  );
};

export default Analytics;

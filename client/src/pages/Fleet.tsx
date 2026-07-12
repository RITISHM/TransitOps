import React, { useState, useEffect, useMemo } from 'react';
import api from '../services/api';
import { useAuthStore } from '../store/authStore';

const Fleet = () => {
  const [vehicles, setVehicles] = useState<any[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [showAddModal, setShowAddModal] = useState(false);
  const { role } = useAuthStore();
  
  // Filtering and Sorting state
  const [searchTerm, setSearchTerm] = useState('');
  const [filterType, setFilterType] = useState('All');
  const [filterStatus, setFilterStatus] = useState('All');
  const [sortConfig, setSortConfig] = useState<{key: string, direction: 'asc'|'desc'} | null>(null);

  const [newVehicle, setNewVehicle] = useState({
    registrationNumber: '',
    vehicleName: '',
    vehicleType: 'TRUCK',
    fuelType: 'DIESEL',
    maxLoadCapacity: '',
    acquisitionCost: '',
    acquisitionDate: new Date().toISOString().split('T')[0],
    regionId: 1
  });

  const fetchVehicles = async () => {
    try {
      setIsLoading(true);
      // Fetch up to 1000 items to allow smooth local sorting and filtering
      const res = await api.get('/vehicles?size=1000');
      if (res.data && res.data.content) {
        setVehicles(res.data.content);
      } else if (Array.isArray(res.data)) {
        setVehicles(res.data);
      } else {
        setVehicles([]);
      }
    } catch (e) {
      console.error('Failed to fetch vehicles', e);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchVehicles();
  }, []);

  const getStatusBadge = (status: string) => {
    if (!status) return 'badge-gray';
    switch (status.toUpperCase()) {
      case 'AVAILABLE': return 'badge-green';
      case 'ON_TRIP': return 'badge-blue';
      case 'IN_SHOP': return 'badge-orange';
      case 'RETIRED': return 'badge-red';
      default: return 'badge-gray';
    }
  };

  const handleAddSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await api.post('/vehicles', {
        registrationNumber: newVehicle.registrationNumber,
        vehicleName: newVehicle.vehicleName,
        vehicleType: newVehicle.vehicleType,
        fuelType: newVehicle.fuelType,
        maxLoadCapacity: parseFloat(newVehicle.maxLoadCapacity),
        acquisitionCost: parseFloat(newVehicle.acquisitionCost),
        acquisitionDate: newVehicle.acquisitionDate,
        regionId: parseInt(newVehicle.regionId as any, 10)
      });
      setShowAddModal(false);
      setNewVehicle({
        registrationNumber: '',
        vehicleName: '',
        vehicleType: 'TRUCK',
        fuelType: 'DIESEL',
        maxLoadCapacity: '',
        acquisitionCost: '',
        acquisitionDate: new Date().toISOString().split('T')[0],
        regionId: 1
      });
      fetchVehicles();
    } catch (err) {
      console.error(err);
      alert('Failed to add vehicle. Please check inputs and ensure registration number is unique.');
    }
  };

  const requestSort = (key: string) => {
    let direction: 'asc' | 'desc' = 'asc';
    if (sortConfig && sortConfig.key === key && sortConfig.direction === 'asc') {
      direction = 'desc';
    }
    setSortConfig({ key, direction });
  };

  // Local filtering & sorting logic
  const processedVehicles = useMemo(() => {
    let result = [...vehicles];

    // Filter by search term
    if (searchTerm) {
      const lower = searchTerm.toLowerCase();
      result = result.filter(v => 
        (v.registrationNumber && v.registrationNumber.toLowerCase().includes(lower)) ||
        (v.vehicleName && v.vehicleName.toLowerCase().includes(lower))
      );
    }

    // Filter by type
    if (filterType !== 'All') {
      result = result.filter(v => v.vehicleType === filterType);
    }

    // Filter by status
    if (filterStatus !== 'All') {
      result = result.filter(v => v.status === filterStatus);
    }

    // Sort
    if (sortConfig) {
      result.sort((a, b) => {
        if (a[sortConfig.key] < b[sortConfig.key]) {
          return sortConfig.direction === 'asc' ? -1 : 1;
        }
        if (a[sortConfig.key] > b[sortConfig.key]) {
          return sortConfig.direction === 'asc' ? 1 : -1;
        }
        return 0;
      });
    }

    return result;
  }, [vehicles, searchTerm, filterType, filterStatus, sortConfig]);

  const renderSortIndicator = (key: string) => {
    if (sortConfig?.key === key) {
      return sortConfig.direction === 'asc' ? ' ↑' : ' ↓';
    }
    return ' ↕';
  };

  if (isLoading) return <div className="loading-state">Loading fleet data...</div>;

  return (
    <div className="fleet-container" style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
      
      {/* Header & Filters */}
      <div className="page-header" style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <div className="filters-row" style={{ display: 'flex', gap: '16px' }}>
          <select className="filter-select" value={filterType} onChange={(e) => setFilterType(e.target.value)}>
            <option value="All">Type: All</option>
            <option value="TRUCK">TRUCK</option>
            <option value="VAN">VAN</option>
            <option value="BUS">BUS</option>
            <option value="PICKUP">PICKUP</option>
            <option value="TRAILER">TRAILER</option>
            <option value="OTHER">OTHER</option>
          </select>
          <select className="filter-select" value={filterStatus} onChange={(e) => setFilterStatus(e.target.value)}>
            <option value="All">Status: All</option>
            <option value="AVAILABLE">AVAILABLE</option>
            <option value="ON_TRIP">ON_TRIP</option>
            <option value="IN_SHOP">IN_SHOP</option>
            <option value="RETIRED">RETIRED</option>
          </select>
          <div className="search-bar" style={{ width: '250px' }}>
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" style={{ marginRight: '8px', color: '#9ca3af' }}><circle cx="11" cy="11" r="8"></circle><line x1="21" y1="21" x2="16.65" y2="16.65"></line></svg>
            <input type="text" placeholder="Search reg. no or name..." className="search-input" value={searchTerm} onChange={(e) => setSearchTerm(e.target.value)} />
          </div>
        </div>
        
        {role === 'FLEET_MANAGER' && (
          <button 
            className="btn btn-primary" 
            style={{ width: 'auto', padding: '10px 20px', marginTop: 0 }}
            onClick={() => setShowAddModal(true)}
          >
            + Add Vehicle
          </button>
        )}
      </div>

      {/* Table Panel */}
      <div className="panel" style={{ padding: '0', overflow: 'hidden' }}>
        <div className="table-responsive">
          <table className="clean-table" style={{ margin: '0' }}>
            <thead>
              <tr>
                <th style={{ padding: '24px', cursor: 'pointer', userSelect: 'none' }} onClick={() => requestSort('registrationNumber')}>
                  REG. NO. {renderSortIndicator('registrationNumber')}
                </th>
                <th style={{ cursor: 'pointer', userSelect: 'none' }} onClick={() => requestSort('vehicleName')}>
                  NAME/MODEL {renderSortIndicator('vehicleName')}
                </th>
                <th style={{ cursor: 'pointer', userSelect: 'none' }} onClick={() => requestSort('vehicleType')}>
                  TYPE {renderSortIndicator('vehicleType')}
                </th>
                <th style={{ cursor: 'pointer', userSelect: 'none' }} onClick={() => requestSort('maxLoadCapacity')}>
                  CAPACITY (KG) {renderSortIndicator('maxLoadCapacity')}
                </th>
                <th style={{ cursor: 'pointer', userSelect: 'none' }} onClick={() => requestSort('currentOdometer')}>
                  ODOMETER {renderSortIndicator('currentOdometer')}
                </th>
                <th style={{ cursor: 'pointer', userSelect: 'none' }} onClick={() => requestSort('acquisitionCost')}>
                  ACQ. COST {renderSortIndicator('acquisitionCost')}
                </th>
                <th style={{ cursor: 'pointer', userSelect: 'none' }} onClick={() => requestSort('status')}>
                  STATUS {renderSortIndicator('status')}
                </th>
              </tr>
            </thead>
            <tbody>
              {processedVehicles.length === 0 ? (
                <tr>
                  <td colSpan={7} style={{ textAlign: 'center', padding: '24px' }}>No vehicles match your criteria.</td>
                </tr>
              ) : processedVehicles.map(v => (
                <tr key={v.id}>
                  <td style={{ padding: '16px 24px', fontWeight: 600 }}>{v.registrationNumber}</td>
                  <td>{v.vehicleName}</td>
                  <td>{v.vehicleType}</td>
                  <td>{v.maxLoadCapacity}</td>
                  <td>{v.currentOdometer}</td>
                  <td>${v.acquisitionCost}</td>
                  <td>
                    <span className={`status-badge ${getStatusBadge(v.status)}`}>{v.status}</span>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {/* Add Vehicle Modal */}
      {showAddModal && (
        <div className="modal-overlay" style={{ position: 'fixed', top: 0, left: 0, right: 0, bottom: 0, backgroundColor: 'rgba(0,0,0,0.5)', display: 'flex', alignItems: 'center', justifyContent: 'center', zIndex: 1000 }}>
          <div className="panel" style={{ width: '400px', padding: '24px', backgroundColor: 'var(--bg-primary)' }}>
            <h2 style={{ marginTop: 0, marginBottom: '20px' }}>Add New Vehicle</h2>
            <form onSubmit={handleAddSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
              
              <div className="form-group" style={{ marginBottom: 0 }}>
                <label>Registration Number *</label>
                <input required type="text" className="form-control" value={newVehicle.registrationNumber} onChange={e => setNewVehicle({...newVehicle, registrationNumber: e.target.value})} />
              </div>
              
              <div className="form-group" style={{ marginBottom: 0 }}>
                <label>Vehicle Name/Model</label>
                <input type="text" className="form-control" value={newVehicle.vehicleName} onChange={e => setNewVehicle({...newVehicle, vehicleName: e.target.value})} />
              </div>

              <div style={{ display: 'flex', gap: '16px' }}>
                <div className="form-group" style={{ marginBottom: 0, flex: 1 }}>
                  <label>Type *</label>
                  <select className="form-control" value={newVehicle.vehicleType} onChange={e => setNewVehicle({...newVehicle, vehicleType: e.target.value})}>
                    <option value="TRUCK">TRUCK</option>
                    <option value="VAN">VAN</option>
                    <option value="BUS">BUS</option>
                    <option value="PICKUP">PICKUP</option>
                    <option value="TRAILER">TRAILER</option>
                    <option value="OTHER">OTHER</option>
                  </select>
                </div>
                <div className="form-group" style={{ marginBottom: 0, flex: 1 }}>
                  <label>Fuel Type</label>
                  <input type="text" className="form-control" value={newVehicle.fuelType} onChange={e => setNewVehicle({...newVehicle, fuelType: e.target.value})} />
                </div>
              </div>

              <div style={{ display: 'flex', gap: '16px' }}>
                <div className="form-group" style={{ marginBottom: 0, flex: 1 }}>
                  <label>Capacity (kg) *</label>
                  <input required type="number" step="0.01" min="0.01" className="form-control" value={newVehicle.maxLoadCapacity} onChange={e => setNewVehicle({...newVehicle, maxLoadCapacity: e.target.value})} />
                </div>
                <div className="form-group" style={{ marginBottom: 0, flex: 1 }}>
                  <label>Cost ($) *</label>
                  <input required type="number" step="0.01" min="0" className="form-control" value={newVehicle.acquisitionCost} onChange={e => setNewVehicle({...newVehicle, acquisitionCost: e.target.value})} />
                </div>
              </div>

              <div className="form-group" style={{ marginBottom: 0 }}>
                <label>Region ID * (default 1)</label>
                <input required type="number" className="form-control" value={newVehicle.regionId} onChange={e => setNewVehicle({...newVehicle, regionId: parseInt(e.target.value) || 1})} />
              </div>

              <div style={{ display: 'flex', gap: '12px', justifyContent: 'flex-end', marginTop: '16px' }}>
                <button type="button" className="btn btn-secondary" onClick={() => setShowAddModal(false)} style={{ width: 'auto' }}>Cancel</button>
                <button type="submit" className="btn btn-primary" style={{ width: 'auto', marginTop: 0 }}>Save Vehicle</button>
              </div>
            </form>
          </div>
        </div>
      )}

    </div>
  );
};

export default Fleet;

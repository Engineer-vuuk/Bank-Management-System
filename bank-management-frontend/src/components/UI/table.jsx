import React from 'react';

export const Table = ({ data = [] }) => {  // ✅ Default value to prevent undefined errors
  return (
    <table style={{ width: '100%', borderCollapse: 'collapse' }}>
      <thead>
        <tr>
          <th style={{ border: '1px solid #ddd', padding: '8px' }}>ID</th>
          <th style={{ border: '1px solid #ddd', padding: '8px' }}>Name</th>
          <th style={{ border: '1px solid #ddd', padding: '8px' }}>Status</th>
        </tr>
      </thead>
      <tbody>
        {data.length > 0 ? ( // ✅ Ensure data is not empty before mapping
          data.map((row, index) => (
            <tr key={index}>
              <td style={{ border: '1px solid #ddd', padding: '8px' }}>{row.id}</td>
              <td style={{ border: '1px solid #ddd', padding: '8px' }}>{row.name}</td>
              <td style={{ border: '1px solid #ddd', padding: '8px' }}>{row.status}</td>
            </tr>
          ))
        ) : (
          <tr>
            <td colSpan="3" style={{ border: '1px solid #ddd', padding: '8px', textAlign: 'center' }}>
              No data available
            </td>
          </tr>
        )}
      </tbody>
    </table>
  );
};

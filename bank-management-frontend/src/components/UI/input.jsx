// Input.jsx
import React from 'react';

export const Input = ({ type = "text", placeholder, value, onChange }) => {
  return (
    <input
      type={type}
      placeholder={placeholder}
      value={value}
      onChange={onChange}
      className="border p-2 rounded-md w-full"
    />
  );
};

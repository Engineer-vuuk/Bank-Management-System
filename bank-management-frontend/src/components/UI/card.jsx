import React from "react";

export const Card = ({ children, style, className = "" }) => {
  return (
    <div
      className={`bg-white/80 p-5 rounded-lg shadow-md backdrop-blur-sm border border-gray-300 ${className}`}
      style={{ ...style }}
    >
      {children}
    </div>
  );
};

export const CardContent = ({ children, className = "" }) => {
  return <div className={`p-4 ${className}`}>{children}</div>;
};

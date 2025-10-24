import { ReactNode } from 'react';

interface CardProps {
  children: ReactNode;
  className?: string;
  hover?: boolean;
}

export const Card = ({ children, className = '', hover = false }: CardProps) => {
  return (
    <div
      className={`bg-white rounded-lg shadow-md ${
        hover ? 'transition-shadow hover:shadow-xl' : ''
      } ${className}`}
    >
      {children}
    </div>
  );
};

export const CardHeader = ({ children, className = '' }: { children: ReactNode; className?: string }) => {
  return <div className={`px-6 py-4 border-b border-gray-200 ${className}`}>{children}</div>;
};

export const CardBody = ({ children, className = '' }: { children: ReactNode; className?: string }) => {
  return <div className={`px-6 py-4 ${className}`}>{children}</div>;
};

export const CardFooter = ({ children, className = '' }: { children: ReactNode; className?: string }) => {
  return <div className={`px-6 py-4 border-t border-gray-200 ${className}`}>{children}</div>;
};

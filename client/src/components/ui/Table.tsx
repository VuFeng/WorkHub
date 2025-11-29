import { type ReactNode } from "react";

interface TableProps {
  children: ReactNode;
  className?: string;
}

interface TableHeaderProps {
  children: ReactNode;
}

interface TableBodyProps {
  children: ReactNode;
}

interface TableRowProps {
  children: ReactNode;
  className?: string;
}

interface TableHeadProps {
  children: ReactNode;
  className?: string;
}

interface TableCellProps {
  children: ReactNode;
  className?: string;
}

const Table = ({ children, className = "" }: TableProps) => {
  return (
    <table className={`min-w-full divide-y divide-gray-200 ${className}`}>
      {children}
    </table>
  );
};

const TableHeader = ({ children }: TableHeaderProps) => {
  return <thead className="bg-gray-50">{children}</thead>;
};

const TableBody = ({ children }: TableBodyProps) => {
  return <tbody className="bg-white divide-y divide-gray-200">{children}</tbody>;
};

const TableRow = ({ children, className = "" }: TableRowProps) => {
  return <tr className={className}>{children}</tr>;
};

const TableHead = ({ children, className = "" }: TableHeadProps) => {
  return (
    <th
      className={`px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider ${className}`}
    >
      {children}
    </th>
  );
};

const TableCell = ({ children, className = "" }: TableCellProps) => {
  return (
    <td className={`px-4 py-4 whitespace-nowrap text-sm text-gray-900 ${className}`}>
      {children}
    </td>
  );
};

Table.Header = TableHeader;
Table.Body = TableBody;
Table.Row = TableRow;
Table.Head = TableHead;
Table.Cell = TableCell;

export default Table;


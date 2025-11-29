import React, { type ReactNode, createContext, useContext } from "react";
import Modal from "./Modal";

interface DialogContextType {
  open: boolean;
  onOpenChange: (open: boolean) => void;
}

const DialogContext = createContext<DialogContextType | null>(null);

interface DialogProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  children: ReactNode;
}

interface DialogTriggerProps {
  asChild?: boolean;
  children: ReactNode;
  onClick?: () => void;
}

interface DialogContentProps {
  className?: string;
  children: ReactNode;
  size?: "sm" | "md" | "lg" | "xl";
}

interface DialogHeaderProps {
  children: ReactNode;
}

interface DialogTitleProps {
  children: ReactNode;
}

const Dialog = ({ open, onOpenChange, children }: DialogProps) => {
  return (
    <DialogContext.Provider value={{ open, onOpenChange }}>
      {children}
    </DialogContext.Provider>
  );
};

const DialogTrigger = ({ asChild, children, onClick }: DialogTriggerProps) => {
  const context = useContext(DialogContext);

  const handleClick = () => {
    onClick?.();
    context?.onOpenChange(true);
  };

  if (asChild && React.isValidElement(children)) {
    return React.cloneElement(
      children as React.ReactElement<{ onClick?: () => void }>,
      { onClick: handleClick }
    );
  }
  return (
    <div onClick={handleClick} className="inline-block">
      {children}
    </div>
  );
};

const DialogContent = ({
  className = "",
  children,
  size = "lg",
}: DialogContentProps) => {
  const context = useContext(DialogContext);

  if (!context?.open) return null;

  return (
    <Modal
      isOpen={context.open}
      onClose={() => context.onOpenChange(false)}
      size={size}
    >
      <div className={className}>{children}</div>
    </Modal>
  );
};

const DialogHeader = ({ children }: DialogHeaderProps) => {
  return <div className="mb-4">{children}</div>;
};

const DialogTitle = ({ children }: DialogTitleProps) => {
  return <h2 className="text-xl font-semibold text-gray-900">{children}</h2>;
};

Dialog.Trigger = DialogTrigger;
Dialog.Content = DialogContent;
Dialog.Header = DialogHeader;
Dialog.Title = DialogTitle;

export default Dialog;

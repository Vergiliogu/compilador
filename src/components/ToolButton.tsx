interface ToolButtonProps {
  iconPath: string;
  label: string;
  onClick: () => void;
}

export const ToolButton = ({
  iconPath,
  label,
  onClick,
}: ToolButtonProps) => {
  return (
    <button className="h-16 w-16" onClick={onClick}>
      <div className="flex flex-col items-center">
        <img src={iconPath} alt={label} className="h-6 w-6" />
        <span className="text-xs">{label}</span>
      </div>
    </button>
  );
}

import { useState } from "react";
import { ImageOff } from "lucide-react";

interface LogoImageProps {
  url: string;
  alt: string;
}

export const LogoImage = ({ url, alt }: LogoImageProps) => {
  const [hasError, setHasError] = useState(false);

  if (hasError) {
    return (
      <div className="h-8 w-8 flex items-center justify-center rounded border border-gray-200 bg-gray-100">
        <ImageOff size={16} className="text-gray-400" />
      </div>
    );
  }

  return (
    <a
      href={url}
      target="_blank"
      rel="noopener noreferrer"
      className="inline-block"
    >
      <img
        src={url}
        alt={alt}
        className="h-8 w-8 object-cover rounded border border-gray-200 hover:opacity-80 transition-opacity"
        onError={() => setHasError(true)}
      />
    </a>
  );
};


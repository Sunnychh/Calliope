export interface NativeModulesType {
  PermissionCheck: {
    checkOverlayPermission(callback: (hasPermission: boolean) => void): void;
    requestOverlayPermission(): void;
    checkAccessibilityPermission(callback: (isEnabled: boolean) => void): void;
    requestAccessibilityPermission(): void;
  };

  FloatingWindowService: {
    showFloatWindow(width: number, height: number, left: number, top: number): void;
    hideFloatWindow(): void;
    moveFloatWindow(left: number, top: number, moveDone: (left: number, top: number) => void): void;
  };
}

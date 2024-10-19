import { Image, StyleSheet, Platform, View, Text, Button, NativeModules, Alert } from 'react-native';

import { NativeModulesType } from '@/types/NativeModules';

const { PermissionCheck, FloatingWindowService } = NativeModules as NativeModulesType;

const getPermissions = async () => {
  console.log
  const hasOverlayPermission = await new Promise<boolean>((resolve) => {
    PermissionCheck.checkOverlayPermission((hasPermission: boolean) => {
      resolve(hasPermission);
    });
  });
  if (hasOverlayPermission) {
    Alert.alert('已获得悬浮窗权限');
  } else {
    PermissionCheck.requestOverlayPermission();
  }

  const hasAccessibilityPermission = await new Promise<boolean>((resolve) => {
    PermissionCheck.checkAccessibilityPermission((hasPermission: boolean) => {
      resolve(hasPermission);
    });
  });
  if (hasAccessibilityPermission) {
    Alert.alert('已获得悬浮窗权限');
  } else {
    PermissionCheck.requestAccessibilityPermission();
  }
}

export default function HomeScreen() {

  const handleAcquirePermissionClick = async () => {
    console.log("handleAcquirePermissionClick");
    await getPermissions();
  }

  const handleShowFloatingWindowClick = () => {
    console.log("handleHideFloatingWindowClick");
    FloatingWindowService.showFloatWindow(50, 50, 0, 20);
  }


  const handleHideFloatingWindowClick = () => {
    console.log("handleHideFloatingWindowClick");
    FloatingWindowService.hideFloatWindow();
  }

  return (
    <View style={styles.container}>
      <Text style={styles.text}>Calliope 调试界面</Text>
      <View style={styles.buttons}>
        <Button title="获取权限" onPress={handleAcquirePermissionClick} />
        <Button title="显示浮窗" onPress={handleShowFloatingWindowClick} />
        <Button title="隐藏浮窗" onPress={handleHideFloatingWindowClick} />
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    position: "relative",
    paddingTop: 24,
  },
  text: {
    fontSize: 24,
    margin: 32,
  },
  buttons: {
    display: "flex",
    flexDirection: "row",
    justifyContent: "center",
    alignItems: "center",
    gap: 24,
  },
  button: {
    flex: 1,
  }
});

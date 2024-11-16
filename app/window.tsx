import { clamp } from "@/tools/tool";
import { NativeModulesType } from "@/types/NativeModules";
import React, { useEffect, useRef } from "react";
import {
  View,
  Text,
  StyleSheet,
  AppRegistry,
  GestureResponderEvent,
  NativeModules,
  PanResponder,
  PanResponderInstance,
  Dimensions,
} from "react-native";

const { FloatingWindowService } = NativeModules as NativeModulesType;

type InitialProps = {
  size: [number, number];
  initPosition: [number, number];
};

export const FloatWindowComponent = (props: InitialProps) => {
  const posRef = useRef({
    initMouseGlobal: [0, 0],
    initMouseLocal: [0, 0],
    initWindowGlobal: props.initPosition,
    newWindowGlobal: props.initPosition,
    windowSize: ((window) => [window.width, window.height] as [number, number])(
      Dimensions.get("window")
    ),

    isDragging: false,
    isMoving: false,
  });

  const handleTouchStart = (e: GestureResponderEvent) => {
    console.log("handleTouchStart", e.nativeEvent);
    if (e.nativeEvent.identifier.toString() !== "0") {
      return;
    }
    posRef.current.initMouseLocal = [e.nativeEvent.pageX, e.nativeEvent.pageY];
    posRef.current.initMouseGlobal = [
      e.nativeEvent.pageX + posRef.current.initWindowGlobal[0],
      e.nativeEvent.pageY + posRef.current.initWindowGlobal[1],
    ];
    posRef.current.isDragging = true;
  };

  const handleTouchMove = (e: GestureResponderEvent) => {
    if (e.nativeEvent.identifier.toString() !== "0") {
      return;
    }
    if (!posRef.current.isDragging || posRef.current.isMoving) {
      return;
    }

    const newWindowGlobal: [number, number] = [
      clamp(
        2 * posRef.current.initWindowGlobal[0] +
          e.nativeEvent.pageX -
          posRef.current.initMouseGlobal[0],
        0,
        posRef.current.windowSize[0] - props.size[0]
      ),
      clamp(
        2 * posRef.current.initWindowGlobal[1] +
          e.nativeEvent.pageY -
          posRef.current.initMouseGlobal[1],
        0,
        posRef.current.windowSize[1] - props.size[1]
      ),
    ];

    // console.log(
    //   `move: mouseLocal=(${Math.floor(e.nativeEvent.locationX)}, ${Math.floor(e.nativeEvent.locationY)}), mouseLocal2=(${Math.floor(e.nativeEvent.pageX)}, ${Math.floor(e.nativeEvent.pageY)}), mouseGlobal=(${Math.floor(posRef.current.initMouseGlobal[0])}, ${Math.floor(posRef.current.initMouseGlobal[1])}), windowGlobal=(${Math.floor(posRef.current.initWindowGlobal[0])}, ${Math.floor(posRef.current.initWindowGlobal[1])}), lastWindowGlobal=(${Math.floor(posRef.current.newWindowGlobal[0])}, ${Math.floor(posRef.current.newWindowGlobal[1])}), newWindowGlobal=(${Math.floor(newWindowGlobal[0])}, ${Math.floor(newWindowGlobal[1])})`
    // );

    posRef.current.newWindowGlobal = newWindowGlobal;
    posRef.current.initMouseGlobal = [
      posRef.current.initWindowGlobal[0] + e.nativeEvent.pageX,
      posRef.current.initWindowGlobal[1] + e.nativeEvent.pageY,
    ];

    moveWindow();
  };

  const handleTouchEnd = (e: GestureResponderEvent) => {
    console.log("handleTouchEnd", e.nativeEvent);
    if (e.nativeEvent.identifier.toString() !== "0") {
      return;
    }
    posRef.current.isDragging = false;
  };

  const moveWindow = () => {
    posRef.current.isMoving = true;
    const newWindowGlobal = posRef.current.newWindowGlobal;

    // console.log(
    //   `moving to: x=${Math.floor(newWindowGlobal[0])}, y=${Math.floor(newWindowGlobal[1])}`
    // );

    FloatingWindowService.moveFloatWindow(
      newWindowGlobal[0],
      newWindowGlobal[1],
      () => {
        posRef.current.initMouseGlobal = [
          newWindowGlobal[0] + posRef.current.initMouseLocal[0],
          newWindowGlobal[1] + posRef.current.initMouseLocal[1],
        ];
        posRef.current.initWindowGlobal = newWindowGlobal;

        setTimeout(() => {
          requestAnimationFrame(() => {
            posRef.current.isMoving = false;
          });
        }, 16);
      }
    );
  };

  useEffect(() => {
    console.log(props, Dimensions.get("window"), Dimensions.get("screen"));
  }, []);

  return (
    <View
      style={styles.container}
      onTouchStart={handleTouchStart}
      onTouchMove={handleTouchMove}
      onTouchEnd={handleTouchEnd}
    >
      <View style={styles.circle} />
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    width: 50,
    height: 50,
    backgroundColor: "rgba(0, 0, 0, 0.7)",
    borderRadius: 100,
    overflow: "hidden",
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
  },
  text: {
    color: "white",
  },
  circle: {
    position: "relative",
    width: 32,
    height: 32,
    backgroundColor: "#ffffffd0",
    borderRadius: 100,
  },
});

export default FloatWindowComponent;

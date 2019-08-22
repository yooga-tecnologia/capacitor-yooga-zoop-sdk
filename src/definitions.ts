declare module "@capacitor/core" {
  interface PluginRegistry {
    YoogaZoopSDK: YoogaZoopSDKPlugin;
  }
}

export interface YoogaZoopSDKPlugin {
  echo(options: { value: string }): Promise<{value: string}>;
}

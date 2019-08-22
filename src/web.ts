import { WebPlugin } from '@capacitor/core';
import { YoogaZoopSDKPlugin } from './definitions';

export class YoogaZoopSDKWeb extends WebPlugin implements YoogaZoopSDKPlugin {
  constructor() {
    super({
      name: 'YoogaZoopSDK',
      platforms: ['web']
    });
  }

  async echo(options: { value: string }): Promise<{value: string}> {
    console.log('ECHO', options);
    return options;
  }
}

const YoogaZoopSDK = new YoogaZoopSDKWeb();

export { YoogaZoopSDK };

import { registerWebPlugin } from '@capacitor/core';
registerWebPlugin(YoogaZoopSDK);

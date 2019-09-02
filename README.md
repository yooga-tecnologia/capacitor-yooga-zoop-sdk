# capacitor-yooga-zoop-sdk
Encapsulamento via Capacitor para integrar com o Zoop Java SDK.

Todas contribuições são bem vindas, o plugin ainda está em **processo de desenvolvimento** e o objetivo é encapsular o máximo possível e tornar simples e funcional no ambiente híbrido do Ionic

## Installation

```bash
$ npm i --save capacitor-yooga-zoop-sdk
```
## Android configuration

In file `android/app/src/main/java/**/**/MainActivity.java`, add the plugin to the initialization list:

```java
this.init(savedInstanceState, new ArrayList<Class<? extends Plugin>>() {{
  [...]
  add(com.yooga.zoop.sdk.YoogaZoopSDK.class);
  [...]
}});

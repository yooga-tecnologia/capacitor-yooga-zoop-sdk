
  Pod::Spec.new do |s|
    s.name = 'YoogaZoopSdk'
    s.version = '0.0.1'
    s.summary = 'Encapsulamento via Capacitor para integrar com o Zoop Java SDK'
    s.license = 'MIT'
    s.homepage = 'https://github.com/yooga-tecnologia/yooga-zoop-sdk.git'
    s.author = 'Yooga Tecnologia'
    s.source = { :git => 'https://github.com/yooga-tecnologia/yooga-zoop-sdk.git', :tag => s.version.to_s }
    s.source_files = 'ios/Plugin/**/*.{swift,h,m,c,cc,mm,cpp}'
    s.ios.deployment_target  = '11.0'
    s.dependency 'Capacitor'
  end
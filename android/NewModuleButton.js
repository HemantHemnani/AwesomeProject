import React from 'react';
import { NativeEventEmitter, NativeModules, Button } from 'react-native';
import { DeviceEventEmitter } from 'react-native';
import {
  ToastAndroid,
  Platform
} from "react-native";

const NewModuleButton = () => {

    const { CalendarModule } = NativeModules;

  const onPress = () => {
    console.log('We will invoke the native module here!');
    CalendarModule.createCalendarEvent('testName', 'testLocation');
    CalendarModule.showMap('testName');
    CalendarModule.navigateToExample();
    getValue();
    ToastAndroid.show("message", ToastAndroid.SHORT);
  };

  const getValue = () =>{
  //   DeviceEventEmitter.addListener('showResult', function (e: Event) {   
  //     // hadle the event triggered from java side
  // });

  DeviceEventEmitter.addListener("onLocationChange", async (e) => 
  {
      // do something
      console.log("RRRRRRR: "+e);
      // CalendarModule.createCalendarEvent('testName', 'eeee latitude: '+Object.values(e)[0]+"  longitude: "+Object.values(e)[1]);
      // CalendarModule.showMap('testName1111111');
      ToastAndroid.show("message "+Object.values(e)[0]+" : "+Object.values(e)[1], ToastAndroid.SHORT);
  })

  // DeviceEventEmitter.addListener("onLocationChange", async (e) => 
  // {
  //     // do something
  //     console.log("RRRRRRR: "+e);
  // })


  }

 

  return (
    <Button
      title="Click to invoke your native module!"
      color="#841584"
      onPress={onPress}
    />
    
  );
};

export default NewModuleButton;
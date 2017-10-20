import React, {Component} from 'react';
import LoginView from "./LoginView";
import Home from "./Home";
import SplashScreen from "./SplashScreen";
import { StackNavigator, TabNavigator } from 'react-navigation';

const AppNavigator = StackNavigator(
    {
      Login:
          {screen: LoginView,
            headerMode: 'none',
            header: null,
            headerTintColor: '#000000',
            navigationOptions: {
              header: null
            }
          },
      Home:
          {screen: Home,
            headerMode: 'none',
            header: null,
            headerTintColor: '#000000',
            navigationOptions: {
              header: null
            }
          },
      SplashScreen: {
        screen: SplashScreen,
        headerMode: 'none',
        header: null,
        headerTintColor: '#000000',
        navigationOptions: {
          header: null
        }
      }
    },
    {
      index: 0,
      initialRouteName: 'SplashScreen',
      mode: 'modal',
      navigationOptions: {
        gesturesEnabled: false
      }
    });

export default AppNavigator;
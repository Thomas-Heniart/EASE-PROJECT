import React, {Component} from 'react';
import Expo from 'expo';
import { StyleSheet, View, TextInput, Image } from 'react-native';
import { Item, Input, Container, Header, Content, Button, Text, Left,Right, Icon, Body, Title, Root } from 'native-base';
import {connect} from "react-redux";
import {bindActionCreators} from "redux";
import {ActionCreators} from "../actions/index";
import LoginView from "./LoginView";
import Home from "./Home";
import SplashScreen from "./SplashScreen";
import { createTransition, FlipX } from 'react-native-transition';
import { StackNavigator, TabNavigator } from 'react-navigation';

const SimpleApp = StackNavigator(
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
      initialRouteName: 'SplashScreen'
    });

class RootView extends Component {
  constructor(props){
    super(props);
  }
  render(){
    return (
        <Root>
          <SimpleApp/>
        </Root>
    )
  }
}

function mapDispatchToProps(dispatch) {
  return bindActionCreators(ActionCreators, dispatch);
}

export default connect(store => ({}), mapDispatchToProps)(RootView);
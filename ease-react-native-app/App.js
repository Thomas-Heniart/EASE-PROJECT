import React, {Component} from 'react';
import Expo from 'expo';
import { StyleSheet, View, TextInput, Image } from 'react-native';
import { StyleProvider, Container, Header, Content, Button, Text, Left,Right, Icon, Body, Title, Root } from 'native-base';
import getTheme from "./native-base-theme/components";
import material from "./native-base-theme/variables/platform";
import {Provider} from "react-redux";
import store from "./app/store";
import {connect} from "react-redux";
import RootView from "./app/component/RootView";

export default class App extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      fontLoaded: false
    };
  }
  async componentDidMount() {
    await Expo.Font.loadAsync({
      'museo': require('./assets/fonts/museo-sans-300.otf'),
      'museo_Bold': require('./assets/fonts/museo-sans-700.otf')
    });
    this.setState({fontLoaded: true});
  }
  render() {
    return (
        <Provider store={store}>
          <StyleProvider style={getTheme(material)}>
            <Container>
              {this.state.fontLoaded ?
                  <RootView/>:
                  null}
            </Container>
          </StyleProvider>
        </Provider>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#EFEFEF',
    alignItems: 'center',
    justifyContent: 'center',
    padding: 25
  },
  input: {
    width: '100%',
    height: 40,
    paddingLeft: 15,
    paddingRight: 5
  }
});

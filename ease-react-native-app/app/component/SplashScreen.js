import React, {Component} from 'react';
import Expo from 'expo';
import { StyleSheet, View, TextInput, Image } from 'react-native';
import { Item, Input, Container, Header, Content, Button, Text, Left,Right, Icon, Body, Title, Root } from 'native-base';
import {connect} from "react-redux";
import {bindActionCreators} from "redux";
import {ActionCreators} from "../actions/index";
import { NavigationActions } from 'react-navigation';

class SplashScreen extends Component {
  resolveView = () => {
    const resetAction = NavigationActions.reset({
      index: 0,
      actions: [
        NavigationActions.navigate({ routeName: 'Login'})
      ],
      key: null
    });
    this.props.navigation.dispatch(resetAction);
  };
  componentDidMount(){
    setTimeout(this.resolveView, 500);
  }
  render(){
    return (
        <Container style={styles.container}>
          <Image
              style={{width:110, height:110}}
              source={require('../resources/images/ease-white-logo-square.png')}
          />
        </Container>
    )
  }
}

const styles = StyleSheet.create({
  container: {
    flex:1,
    paddingLeft: 30,
    paddingRight: 30,
    paddingBottom: 40,
    alignItems:'center',
    justifyContent:'center',
    backgroundColor:'#373B60'
  }
});

function mapDispatchToProps(dispatch) {
  return bindActionCreators(ActionCreators, dispatch);
}

export default connect(store => ({}), mapDispatchToProps)(SplashScreen);
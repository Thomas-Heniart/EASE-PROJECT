import React, {Component} from 'react';
import Expo from 'expo';
import { StyleSheet, View, TextInput, Image } from 'react-native';
import { Spinner,Item, Input, Container, Header, Content, Button, Text, Left,Right, Icon, Body, Title, Root } from 'native-base';
import {connect} from "react-redux";
import {bindActionCreators} from "redux";
import {ActionCreators} from "../actions/index";
import {resetNavigation, alertToast} from "../utils/helpersFunctions";
import {AsyncStorage} from "react-native";

class SplashScreen extends Component {
  resolveView = () => {
    AsyncStorage.getItem('JWTToken').then(value => {
      if (value !== null)
        this.props.connectionWithJWTToken({token: value})
            .then(() => {
              this.props.fetchSpaces()
                  .then(() => {
                    this.props.navigation.navigate('Home');
                  })
                  .catch(err => {
                    this.props.navigation.navigate('Login');
                  });
            })
            .catch(err => {
              this.props.navigation.navigate('Login');
            });
      else
        this.props.navigation.navigate('Login');
    }).catch(err => {
      this.props.navigation.navigate('Login');
    });
  };
  componentDidMount(){
    setTimeout(() => {
      this.resolveView();
    }, 500);
  }
  render(){
    return (
        <Container style={{flex:1}}>
          <View style={styles.container}>
            <Spinner/>
          </View>
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
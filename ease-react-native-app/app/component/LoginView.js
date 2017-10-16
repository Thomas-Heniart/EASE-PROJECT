import React, {Component} from 'react';
import Expo from 'expo';
import { StyleSheet, View, TextInput, Image } from 'react-native';
import { Toast, Item, Input, Container, Header, Content, Button, Text, Left,Right, Icon, Body, Title, Root } from 'native-base';
import {connect} from "react-redux";
import {AsyncStorage} from "react-native";
import {bindActionCreators} from "redux";
import {ActionCreators} from "../actions/index";
import {resetNavigation,alertToast} from "../utils/helpersFunctions";

class LoginView extends Component {
  constructor(props){
    super(props);
    this.state = {
      email: '',
      password: ''
    }
  }
  connect = () => {
    this.props.connection({
      email: this.state.email,
      password: this.state.password
    }).then(response => {
      this.props.fetchSpaces().then(() => {
        resetNavigation(this.props.navigation, 'Home');
      }).catch(err => {
        alertToast(err, 'Ok!',2000);
      });
    }).catch(err => {
      alertToast(err, 'Ok!', 2000);
    });
  };
  componentDidMount() {
    AsyncStorage.getItem('LastEmail').then(value => {
      if (value !== null){
        this.setState({email: value});
      }
    })
  }
  render(){
    return (
        <Container style={{flex:1}}>
          <View style={{position:'absolute', width: '100%', height: '100%'}}>
            <Image style={{width:'100%', height:'100%'}} source={require('../resources/images/blue_background.png')}/>
          </View>
          <View style={styles.container}>
            <View style={{flex:1}}/>
            <Text>
              {this.state.clicked}
            </Text>
            <Image
                style={{width:110, height:110, marginBottom:60}}
                source={require('../resources/images/logo_hexagone_white.png')}/>
            <Item regular style={styles.inputWrapper}>
              <Input placeholder='Email'
                     style={{color: "white"}}
                     keyboardType="email-address"
                     placeholderTextColor="#D2DAE4"
                     onChangeText={(email) => this.setState({email: email})}/>
            </Item>
            <Item regular style={styles.inputWrapper}>
              <Input placeholder='Password'
                     placeholderTextColor="#D2DAE4"
                     style={{color: "white"}}
                     secureTextEntry={true}
                     onChangeText={(password) => this.setState({password: password})}/>
            </Item>
            <View style={{flex:1}}/>
            <Button block onPress={this.connect} style={{backgroundColor:"#45C997", height:50, borderRadius:5}}>
              <Text style={{fontWeight:'bold'}}>Connection</Text>
            </Button>
          </View>
        </Container>
    )
  }
}

function mapDispatchToProps(dispatch) {
  return bindActionCreators(ActionCreators, dispatch);
}

const styles = StyleSheet.create({
  container: {
    flex:1,
    paddingLeft: 30,
    paddingRight: 30,
    paddingBottom: 40,
    alignItems:'center',
    justifyContent:'center',
    backgroundColor:'transparent'
  },
  inputWrapper: {
    marginBottom:30,
    borderLeftWidth:0,
    borderTopWidth:0,
    borderRightWidth:0,
    borderBottomColor:"#d2dae4",
    borderBottomWidth: 1
  }
});

export default connect(store => ({}), mapDispatchToProps)(LoginView);
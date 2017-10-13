import React, {Component} from 'react';
import Expo from 'expo';
import { StyleSheet, View, TextInput, Image } from 'react-native';
import { Toast, Item, Input, Container, Header, Content, Button, Text, Left,Right, Icon, Body, Title, Root } from 'native-base';
import {connect} from "react-redux";
import {bindActionCreators} from "redux";
import {ActionCreators} from "../actions/index";
import {NavigationActions} from 'react-navigation';
import api from "../utils/api";

class LoginView extends Component {
  constructor(props){
    super(props);
    this.state = {
      email: '',
      password: ''
    }
  }
  connect = () => {
    console.log('connection');
    if (this.state.email.length === 0)
      console.log('batard');
    console.log(this.state.email);
    console.log(this.state.password);
    this.props.connection({
      email: this.state.email,
      password: this.state.password
    }).then(response => {
      const resetAction = NavigationActions.reset({
        index: 0,
        actions: [
          NavigationActions.navigate({ routeName: 'Home'})
        ]
      });
      this.props.navigation.dispatch(resetAction);
    }).catch(err => {
      console.log(err);
      Toast.show({
        text: err,
        position: 'bottom',
        buttonText: 'OK!',
        duration: 2000,
        type:'danger'
      });
    });
//        const {navigate} = this.props.navigation;
    //   navigate('Home');
    //do connection
  };
  componentDidMount(){
  }
  render(){
    return (
        <Container style={styles.container}>
          <View style={{flex:1}}/>
          <Text>
            {this.state.clicked}
          </Text>
          <Image
              style={{width:110, height:110, marginBottom:60}}
              source={require('../resources/images/ease-white-logo-square.png')}/>
          <Item regular style={styles.inputWrapper}>
            <Input placeholder='Email'
                   style={{color: "white"}}
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
    backgroundColor:'#373B60'
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
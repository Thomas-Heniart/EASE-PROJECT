import React, {Component} from 'react';
import Expo from 'expo';
import { StyleSheet, View, TextInput, Image, ScrollView } from 'react-native';
import { Toast, Item, Input, Container, Header, Content, Text, Left,Right, Icon, Body, Title, Root } from 'native-base';
import {connect} from "react-redux";
import {AsyncStorage} from "react-native";
import {bindActionCreators} from "redux";
import {ActionCreators} from "../actions/index";
import Button from 'apsl-react-native-button';
import {resetNavigation,alertToast} from "../utils/helpersFunctions";
import KeyboardSpacer from "react-native-keyboard-spacer";

class LoginView extends Component {
  constructor(props){
    super(props);
    this.state = {
      email: '',
      password: '',
      loading: false
    }
  }
  connect = () => {
    this.setState({loading: true});
    this.props.connection({
      email: this.state.email,
      password: this.state.password
    }).then(response => {
      this.props.fetchSpaces().then(() => {
        this.setState({loading: false});
        this.props.navigation.navigate('Home');
      }).catch(err => {
        this.setState({loading: false});
        alertToast(err, 'Ok!',2000);
      });
    }).catch(err => {
      this.setState({loading: false});
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
          <ScrollView contentContainerStyle={styles.container} scrollEnabled={false}>
            <View style={{flex:1}}/>
            <Image
                style={{width:110, height:110, marginBottom:60}}
                source={require('../resources/images/logo_hexagone_white.png')}/>
            <View style={{width: '100%'}}>
              <Item regular style={styles.inputWrapper}>
                <Input placeholder='Email'
                       style={{color: "white"}}
                       keyboardType="email-address"
                       returnKeyType="next"
                       onSubmitEditing={() => this.secondInput._root.focus()}
                       placeholderTextColor="#D2DAE4"
                       value={this.state.email}
                       onChangeText={(email) => this.setState({email: email})}/>
              </Item>
              <Item regular style={styles.inputWrapper}>
                <Input placeholder='Password'
                       placeholderTextColor="#D2DAE4"
                       ref={(ref) => {this.secondInput = ref}}
                       returnKeyType="go"
                       style={{color: "white"}}
                       secureTextEntry={true}
                       onSubmitEditing={() => this.connect()}
                       value={this.state.password}
                       onChangeText={(password) => this.setState({password: password})}/>
              </Item>
            </View>
            <View style={{flex:1}}/>
            <Button
                onPress={this.connect}
                textStyle={{fontWeight:'bold', color: 'white'}}
                isLoading={this.state.loading}
                style={{backgroundColor:"#45C997",
                  borderColor:'transparent',
                  height:50,
                  borderRadius:5}}>
              Connection
            </Button>
            <KeyboardSpacer/>
          </ScrollView>
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
    paddingTop: 20,
    backgroundColor:'#373B60',
    alignItems:'center',
    justifyContent: 'space-between',
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
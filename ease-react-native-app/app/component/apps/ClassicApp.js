import React, {Component} from 'react';
import { StyleSheet, View, TextInput, Image, TouchableHighlight, TouchableNativeFeedback, Clipboard   } from 'react-native';
import {BoldText } from "../common/text";
import styles from "./styles";
import { Spinner, Toast, ActionSheet, Card,Text, CardItem, List,ListItem, Item, Input, Drawer, Container, Header, Content, Button, Left,Right, Icon, Body, Title } from 'native-base';

class ClassicApp extends Component {
  showActions = () => {
    const info = this.props.app.account_information;
    this.credentialList = Object.keys(info).map(item => {
      return {
        name: item,
        value: info[item]
      }
    });
    this.buttons = this.credentialList.map(item => {
      return `Copy ${item.name}`
    });
    this.buttons.push('Cancel');
    ActionSheet.show(
        {
          options: this.buttons,
          cancelButtonIndex: this.credentialList.length,
          title: 'What would you like to do?'
        },
        this.handleActionClick
    )
  };
  handleActionClick = (index) => {
    if (index < this.credentialList.length){
      Clipboard.setString(this.credentialList[index].value);
      Toast.show({
        text: `${this.credentialList[index].name} copied`,
        position: 'bottom',
        type: 'success',
        buttonText: 'Ok!',
        duration: 2000
      })
    }
  };
  render(){
    const {app} = this.props;
    return (
        <ListItem style={{backgroundColor:'transparent', borderBottomWidth:0}} key={app.id} button onPress={this.showActions}>
          <View style={{marginRight:10}}>
            <Image source={{uri: 'https://ease.space' + app.logo}} style={styles.websiteLogo}/>
          </View>
          <Content>
            <BoldText style={{fontWeight:'bold', fontSize:18}}>
              {app.name}
            </BoldText>
            <Text style={{fontSize:14}}>
              {app.account_information['login']}
            </Text>
          </Content>
        </ListItem>
    )
  }
}

export default ClassicApp;
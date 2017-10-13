import React, {Component} from 'react';
import { StyleSheet, View, TextInput, Image, TouchableHighlight, TouchableNativeFeedback, Clipboard   } from 'react-native';
import {BoldText } from "../common/text";
import { Spinner, Toast, ActionSheet,Text, Card, CardItem, List,ListItem, Item, Input, Drawer, Container, Header, Content, Button, Left,Right, Icon, Body, Title } from 'native-base';
import styles from "./styles";
const buttons = ["Url", "Cancel"];

class LinkApp extends Component {
  showActions = () => {
    ActionSheet.show(
        {
          options: buttons,
          cancelButtonIndex: 1,
          title: 'What would you like to copy ?'
        },
        this.handleActionClick
    )
  };
  handleActionClick = (index) => {
    if (index == 0){
      Clipboard.setString(this.props.app.url);
      Toast.show({
        text: "Url copied",
        position: 'bottom',
        buttonText: 'Got it!',
        duration: 2000
      });
    }
  };
  render(){
    const {app} = this.props;

    return (
        <ListItem style={{backgroundColor:'transparent', borderBottomWidth:0}} key={app.id} button onPress={this.showActions}>
          <View style={{marginRight:10}}>
            <Image source={{uri: app.logo}} style={styles.websiteLogo}/>
          </View>
          <Content>
            <BoldText style={{fontSize:18}}>
              {app.name}
            </BoldText>
            <Text style={{fontSize:14}}>
              {app.url}
            </Text>
          </Content>
        </ListItem>
    )
  }
}

export default LinkApp;
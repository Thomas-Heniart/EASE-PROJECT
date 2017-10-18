import React, {Component} from 'react';
import { StyleSheet, View,TextInput, Image, TouchableHighlight, TouchableNativeFeedback, TouchableOpacity, Clipboard, ScrollView   } from 'react-native';
import { Text,Toast, ActionSheet, Card, CardItem, List,ListItem, Item, Input, Drawer, Container, Header, Content, Button, Left,Right, Icon, Body, Title } from 'native-base';

class SideBarChooseButton extends Component {
  constructor(props){
    super(props);
    this.state = {
      pressStatus: false
    }
  }
  onHideUnderlay  = () =>{
    this.setState({pressStatus: false});
  };
  onShowUnderlay  = () =>{
    this.setState({pressStatus: true});
  };
  render(){
    const {active} = this.props;

    return (
        <View style={{borderRadius:5}}>
          <TouchableOpacity
              {...this.props}
              onHideUnderlay={this.onHideUnderlay}
              onShowUnderlay={this.onShowUnderlay}>
            <View style={[styles.normal, this.state.pressStatus || active ? styles.pressed : null]}>
              {this.props.children}
            </View>
          </TouchableOpacity>
        </View>
    )
  }
}

const styles = StyleSheet.create({
  pressed: {
    backgroundColor: "#4990e2"
  },
  normal: {
    borderRadius:5,
    overflow:'hidden'
  }
});

export default SideBarChooseButton;
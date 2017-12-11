import React, {Component} from 'react';
import Expo from 'expo';
import { StyleSheet, View,TextInput, Image, TouchableHighlight, TouchableNativeFeedback,TouchableOpacity, Clipboard, ScrollView   } from 'react-native';
import { Text,Toast, ActionSheet, Card, CardItem, List,ListItem, Item, Input, Drawer, Container, Header, Content, Button, Left,Right, Icon, Body, Title } from 'native-base';
import {BoldText } from "./common/text";
import SideBarChooseButton from "./common/SideBarChooseButton";
import {ActionCreators} from "../actions/index";
import {bindActionCreators} from "redux";
import {resetNavigation} from "../utils/helpersFunctions";
import {NavigationActions} from 'react-navigation';
import {connect} from "react-redux";

class PersonalSpaceProfileList extends Component {
  render() {
    const {spaces, selectedItem, selectItem} = this.props;
    const profiles = Object.keys(spaces.profiles).map(id => {
      let profile = spaces.profiles[id];
      profile.id = id;
      return profile;
    });

    return (
        <View style={{marginBottom:15}}>
          <BoldText style={styles.sectionHeader}>Personal space</BoldText>
          {profiles.map(item => {
            return (
                <SideBarChooseButton
                    active={selectedItem.itemId === item.id}
                    key={item.id}
                    onPress={() => {selectItem(item.id)}}>
                  <Text style={styles.subSection}>
                    {item.name}
                  </Text>
                </SideBarChooseButton>
            )
          })}
        </View>
    )
  }
}

class HomeSideBar extends Component {
  logout = () => {
    this.props.logout();
    this.props.navigation.navigate('Login');
  };
  selectItem = (itemId) => {
    this.props.selectItem({
      itemId: itemId
    });
    this.props.close();
  };
  render(){
    return (
        <View style={{backgroundColor:'#373B60', flex:1, paddingTop:20, paddingBottom:20}}>
          <View style={{flexDirection:"row", alignItems:'center', marginBottom:20, paddingLeft:20, paddingRight:20}}>
            <Icon name='contact' style={{color: 'white', marginRight:15}}/>
            <Text style={{color:"white", fontSize:20}}>
              {this.props.auth.username}
            </Text>
          </View>
          <ScrollView style={{flex:1, paddingLeft:20, paddingRight:20, marginBottom:20}}>
            <PersonalSpaceProfileList
                spaces={this.props.spaces}
                selectedItem={this.props.selectedItem}
                selectItem={this.selectItem}
                close={this.props.close}/>
          </ScrollView>
          <View style={{paddingLeft:20, paddingRight:20}}>
            <TouchableHighlight style={styles.mainButtons}>
              <View style={{flexDirection:'row', alignItems:'center'}}>
                <BoldText style={{fontSize:22, color: "white"}}>
                  2 FA
                </BoldText>
                <Image style={{height:17, width:60, marginLeft:20}} source={require('../resources/images/soon_image.png')}/>
              </View>
            </TouchableHighlight>
            <TouchableOpacity style={styles.mainButtons} onPress={this.logout}>
              <BoldText style={{color:'#df5454', fontSize:22}}>
                LOGOUT
              </BoldText>
            </TouchableOpacity>
          </View>
        </View>
    )
  }
}

const styles = StyleSheet.create({
  sectionHeader: {
    color: 'white',
    fontSize:22,
    marginBottom:10
  },
  subSection: {
    color: 'white',
    fontSize:18,
    paddingLeft:5,
    paddingTop:7.5,
    paddingBottom:7.5
  },
  mainButtons: {
    paddingTop:5,
    paddingBottom:5
  }
});

function mapDispatchToProps(dispatch){
  return bindActionCreators(ActionCreators, dispatch);
}

export default connect(store => ({
  auth: store.auth,
  spaces: store.spaces,
  selectedItem: store.selectedItem
}), mapDispatchToProps)(HomeSideBar);
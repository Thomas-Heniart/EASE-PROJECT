import React, {Component} from 'react';
import Expo from 'expo';
import { StyleSheet, View,TextInput, Image, TouchableHighlight, TouchableNativeFeedback, Clipboard, ScrollView   } from 'react-native';
import { Text,Toast, ActionSheet, Card, CardItem, List,ListItem, Item, Input, Drawer, Container, Header, Content, Button, Left,Right, Icon, Body, Title } from 'native-base';
import {BoldText } from "./common/text";
import {ActionCreators} from "../actions/index";
import {bindActionCreators} from "redux";
import {NavigationActions} from 'react-navigation';
import {connect} from "react-redux";

class PersonalSpaceProfileList extends Component {
  render() {
    const {profiles, select} = this.props;
    return (
        <View style={{marginBottom:15}}>
          <BoldText style={styles.sectionHeader}>Personal space</BoldText>
          {profiles.map(item => {
            return (
                <TouchableHighlight key={item.id} onPress={() => {select(-1, item.id, item.name)}}>
                  <Text style={styles.subSection}>
                    {item.name}
                  </Text>
                </TouchableHighlight>
            )
          })}
        </View>
    )
  }
}

class TeamRoomList extends Component {
  render(){
    const {team, select} = this.props;
    const rooms = team.rooms;
    return (
        <View style={{marginBottom:15}}>
          <BoldText style={styles.sectionHeader}>
            {team.name}
          </BoldText>
          {rooms.map(item => {
            return (
                <TouchableHighlight key={item.id} onPress={() => {select(team.id, item.id, item.name)}}>
                  <Text style={styles.subSection}>
                    # {item.name}
                  </Text>
                </TouchableHighlight>
            )
          })}
        </View>
    )
  }
}

class HomeSideBar extends Component {
  logout = () => {
    const resetAction = NavigationActions.reset({
      index: 0,
      actions: [
        NavigationActions.navigate({routeName: 'Login'})
      ]
    });
    this.props.navigation.dispatch(resetAction);
  };
  selectItem = (itemId, subItemId, name) => {
    this.props.selectItem({
      itemId: itemId,
      subItemId: subItemId,
      name: name
    });
    this.props.close();
  };
  render(){
    const spaces = this.props.spaces;
    return (
        <View style={{backgroundColor:'#373B60', flex:1, paddingTop:20, paddingBottom:20}}>
          <View style={{flexDirection:"row", alignItems:'center', marginBottom:20, paddingLeft:20, paddingRight:20}}>
            <Icon name='contact' style={{color: 'white', marginRight:15}}/>
            <Text style={{color:"white"}}>
              {this.props.auth.username}
            </Text>
          </View>
          <ScrollView style={{flex:1, paddingLeft:20, paddingRight:20, marginBottom:20}}>
            <PersonalSpaceProfileList select={this.selectItem} profiles={spaces.personal_space}/>
            {spaces.teams.map(item => {
              return (
                  <TeamRoomList team={item} key={item.id} select={this.selectItem}/>
              )
            })}
          </ScrollView>
          <View style={{paddingLeft:20, paddingRight:20}}>
            <TouchableHighlight onPress={this.logout}>
              <BoldText style={{color:'#df5454', fontSize:16}}>
                LOGOUT
              </BoldText>
            </TouchableHighlight>
          </View>
        </View>
    )
  }
}

const styles = StyleSheet.create({
  sectionHeader: {
    color: 'white',
    fontSize:20,
    marginBottom:10
  },
  subSection: {
    color: 'white',
    marginBottom:5,
    fontSize:16,
    paddingLeft:5
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
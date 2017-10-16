import React, {Component} from 'react';
import Expo from 'expo';
import { StyleSheet, View,TextInput, Image, TouchableHighlight, TouchableNativeFeedback, Clipboard, ScrollView   } from 'react-native';
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
    const {profiles, select} = this.props;
    return (
        <View style={{marginBottom:15}}>
          <BoldText style={styles.sectionHeader}>Personal space</BoldText>
          {profiles.map(item => {
            return (
                <SideBarChooseButton
                    active={this.props.selectedItem.itemId === -1 && this.props.selectedItem.subItemId === item.id}
                    key={item.id}
                    onPress={() => {select(-1, item.id, item.name)}}>
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
                <SideBarChooseButton
                    active={this.props.selectedItem.itemId === team.id && this.props.selectedItem.subItemId === item.id}
                    key={item.id}
                    onPress={() => {select(team.id, item.id, `# ${item.name}`)}}>
                  <Text style={styles.subSection}>
                    # {item.name}
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
    resetNavigation(this.props.navigation, 'Login');
  };
  selectItem = (itemId, subItemId, name) => {
    this.props.selectItemAndFetchApps({
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
            <Text style={{color:"white", fontSize:20}}>
              {this.props.auth.username}
            </Text>
          </View>
          <ScrollView style={{flex:1, paddingLeft:20, paddingRight:20, marginBottom:20}}>
            <PersonalSpaceProfileList
                select={this.selectItem}
                profiles={spaces.personal_space}
                selectedItem={this.props.selectedItem}/>
            {spaces.teams.map(item => {
              return (
                  <TeamRoomList
                      team={item}
                      key={item.id}
                      select={this.selectItem}
                      selectedItem={this.props.selectedItem}/>
              )
            })}
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
            <TouchableHighlight style={styles.mainButtons} onPress={this.logout}>
              <BoldText style={{color:'#df5454', fontSize:22}}>
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
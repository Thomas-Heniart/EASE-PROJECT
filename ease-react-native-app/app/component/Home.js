import React, {Component} from 'react';
import { StyleSheet, ScrollView, View, TextInput, Image,Text, TouchableHighlight, TouchableNativeFeedback, Clipboard   } from 'react-native';
import { Drawer,Spinner, Toast, ActionSheet, Card, CardItem, List,ListItem, Item, Input, Container, Header, Content, Button, Left,Right, Icon, Body, Title } from 'native-base';
import {ActionCreators} from "../actions/index";
import {BoldText } from "./common/text";
import {bindActionCreators} from "redux";
import {connect} from "react-redux";
import HomeSideBar from "./HomeSideBar";
import AppList from "./apps/AppList";

class Home extends Component {
  constructor(props) {
    super(props);
    this.state = {
      inputValue: ''
    };
  }
  closeDrawer = () => {
    this._drawer._root.close();
  };
  openDrawer = () => {
    this._drawer._root.open();
  };
  render(){
    return (
        <Drawer
            ref={(ref) => { this._drawer = ref }}
            type="overlay"
            openDrawerOffset={0.2}
            captureGestures={true}
            negotiatePan={true}
            panCloseMask={0.2}
            panOpenMask={0.1}
            acceptTap={true}
            useInteractionManager={true}
            tweenDuration={100}
            tapToClose={true}
            content={<HomeSideBar navigation={this.props.navigation} close={this.closeDrawer}/>}>
          <Container style={{zIndex:10}}>
            <Header style={{zIndex:10,backgroundColor:"#373B60", height:60, paddingTop:15}}>
              <Left onPress={this.openDrawer}>
                <Button transparent onPress={this.openDrawer}>
                  <Icon name='menu' color="#FFF"/>
                </Button>
              </Left>
              <Body>
              <BoldText style={{color: 'white', textAlign:'left'}} numberOfLines={1}>
                {!!this.props.spaces.profiles[this.props.selectedItem.itemId] &&
                this.props.spaces.profiles[this.props.selectedItem.itemId].name}
              </BoldText>
              </Body>
              <Right/>
            </Header>
            <Content>
              {this.props.selectedItem.itemId !== -1 &&
              <AppList/>}
            </Content>
          </Container>
        </Drawer>
    )
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#EFEFEF',
    alignItems: 'center',
    justifyContent: 'center',
    padding: 25
  },
  input: {
    width: '100%',
    height: 40,
    paddingLeft: 15,
    paddingRight: 5
  },
  websiteLogo: {
    width:50,
    height:50,
    borderRadius:5,
    shadowOffset:{width: 10,height: 10},
    shadowColor: 'black',
    shadowOpacity: 10,
    shadowRadius:10
  }
});

function mapDispatchToProps(dispatch) {
  return bindActionCreators(ActionCreators, dispatch);
}

export default connect(store => ({
  auth: store.auth,
  selectedItem: store.selectedItem,
  spaces: store.spaces
}), mapDispatchToProps)(Home);
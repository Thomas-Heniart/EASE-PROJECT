import React, {Component} from 'react';
import { StyleSheet, View, TextInput, Image,Text, TouchableHighlight, TouchableNativeFeedback, Clipboard   } from 'react-native';
import { Spinner, Toast, ActionSheet, Card, CardItem, List,ListItem, Item, Input, Drawer, Container, Header, Content, Button, Left,Right, Icon, Body, Title } from 'native-base';
import {ActionCreators} from "../actions/index";
import {BoldText } from "./common/text";
import {bindActionCreators} from "redux";
import {connect} from "react-redux";
import HomeSideBar from "./HomeSideBar";
import AppList from "./apps/AppList";
import { NavigationActions } from 'react-navigation';

class Home extends Component {
  constructor(props) {
    super(props);
    this.state = {
      inputValue: ''
    };
  }
  changeUsername = () => {
    this.props.changeUsername({username:this.state.inputValue});
    this.setState({inputValue: ''});
  };
  closeDrawer = () => {
    this.drawer._root.close();
  };
  openDrawer = () => {
    this.drawer._root.open();
  };
  render(){
    return (
        <Drawer
            ref={(ref) => { this.drawer = ref; }}
            content={<HomeSideBar navigation={this.props.navigation} close={this.closeDrawer}/>}
            onClose={() => this.closeDrawer()}>
          <Container>
            <Header style={{backgroundColor:"#373B60", height:60, paddingTop:15}}>
              <Left>
                <Button transparent onPress={this.openDrawer}>
                  <Icon name='menu' />
                </Button>
              </Left>
              <Body>
                <BoldText style={{color: 'white'}}>{this.props.selectedItem.name}</BoldText>
              </Body>
              <Right/>
            </Header>
            <Content>
              {this.props.apps.loading ?
                  <Spinner/> :
                  <AppList apps={this.props.apps.apps}/>}
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
  apps: store.apps,
  spaces: store.spaces
}), mapDispatchToProps)(Home);
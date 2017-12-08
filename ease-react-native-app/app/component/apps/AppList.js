import React, {Component} from 'react';
import {ListView, RefreshControl, StyleSheet, View, TextInput, Image,Text, TouchableHighlight, TouchableNativeFeedback, Clipboard   } from 'react-native';
import { Spinner, Toast, ActionSheet, Card, CardItem, List,ListItem, Item, Input, Drawer, Container, Header, Content, Button, Left,Right, Icon, Body, Title } from 'native-base';
import {connect} from "react-redux";
import {fetchPersonalSpace} from "../../actions/commonActions";
import LinkApp from "./LinkApp";
import ClassicApp from "./ClassicApp";

@connect(store => ({
  spaces: store.spaces,
  selectedItem: store.selectedItem
}))
class AppList extends Component {
  constructor(props){
    super(props);
    this.state = {
      refreshing: false
    }
  }
  onRefresh = () => {
    this.setState({refreshing: true});
    this.props.dispatch(fetchPersonalSpace()).then(response => {
      this.setState({refreshing: false});
    }).catch(err => {
      this.setState({refreshing: false});
    });
  };
  render(){
    const {apps, profiles} = this.props.spaces;
    const {itemId} = this.props.selectedItem;
    const profile = profiles[itemId];
    let profile_apps = [];
    if (!!profile)
      profile_apps = profile.app_ids.map(id => {
        return apps[id];
      });
    return (
        <List
            refreshControl={
              <RefreshControl
                  refreshing={this.state.refreshing}
                  onRefresh={this.onRefresh.bind(this)}
              />
            }
            dataArray={profile_apps}
            renderRow={(item) => {
              if (!!item.url)
                return <LinkApp key={item.id} app={item}/>;
              if (!!item.account_information)
                return <ClassicApp key={item.id} app={item}/>;
              return null;
            }}/>
    )
  }
}

export default AppList;
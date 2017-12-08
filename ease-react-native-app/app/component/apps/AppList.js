import React, {Component} from 'react';
import {ListView, RefreshControl, StyleSheet, View, TextInput, Image,Text, TouchableHighlight, TouchableNativeFeedback, Clipboard   } from 'react-native';
import { Spinner, Toast, ActionSheet, Card, CardItem, List,ListItem, Item, Input, Drawer, Container, Header, Content, Button, Left,Right, Icon, Body, Title } from 'native-base';
import {connect} from "react-redux";
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
  };
  render(){
    const {apps, profiles} = this.props.spaces;
    const {itemId} = this.props.selectedItem;
    const profile_apps = profiles[itemId].app_ids.map(id => {
      return apps[id];
    });
    return (
        <ListView
            refreshControl={
              <RefreshControl
                  refreshing={true}
                  onRefresh={this.onRefresh.bind(this)}
              />
            }
        >
          {profile_apps.map(item => {
            if (!!item.url)
              return <LinkApp key={item.id} app={item}/>;
            if (!!item.account_information)
              return <ClassicApp key={item.id} app={item}/>;
          })}
        </ListView>
    )
  }
}

export default AppList;
import React, {Component} from 'react';
import { StyleSheet, View, TextInput, Image,Text, TouchableHighlight, TouchableNativeFeedback, Clipboard   } from 'react-native';
import { Spinner, Toast, ActionSheet, Card, CardItem, List,ListItem, Item, Input, Drawer, Container, Header, Content, Button, Left,Right, Icon, Body, Title } from 'native-base';
import LinkApp from "./LinkApp";
import ClassicApp from "./ClassicApp";

class AppList extends Component {
  render(){
    const {apps} = this.props;

    return (
        <List pagingEnabled={true}>
          {apps.map(item => {
            if (item.type === 'linkApp')
              return <LinkApp key={item.id} app={item}/>;
            if (item.type === "classicApp")
              return <ClassicApp key={item.id} app={item}/>;
          })}
        </List>
    )
  }
}

export default AppList;
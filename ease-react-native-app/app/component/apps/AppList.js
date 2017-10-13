import React, {Component} from 'react';
import { StyleSheet, View, TextInput, Image,Text, TouchableHighlight, TouchableNativeFeedback, Clipboard   } from 'react-native';
import { Spinner, Toast, ActionSheet, Card, CardItem, List,ListItem, Item, Input, Drawer, Container, Header, Content, Button, Left,Right, Icon, Body, Title } from 'native-base';
import LinkApp from "./LinkApp";
import ClassicApp from "./ClassicApp";

class AppList extends Component {
  render(){
    const {apps} = this.props;

    return (
        <List>
          {apps.map(item => {
            if (item.type === 'link')
              return <LinkApp key={item.id} app={item}/>;
            if (item.type === "app")
              return <ClassicApp key={item.id} app={item}/>;
          })}
        </List>
    )
  }
}

export default AppList;
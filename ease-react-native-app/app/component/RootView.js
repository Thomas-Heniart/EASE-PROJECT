import React, {Component} from 'react';
import Expo from 'expo';
import { NetInfo, StyleSheet, View, TextInput, Image } from 'react-native';
import { Item, Input, Container, Header, Content, Button, Text, Left,Right, Icon, Body, Title, Root } from 'native-base';
import {connect} from "react-redux";
import { BackHandler } from "react-native";
import {bindActionCreators} from "redux";
import { NavigationActions } from 'react-navigation';
import { addNavigationHelpers } from 'react-navigation';
import {ActionCreators} from "../actions/index";
import AppNavigator from "./AppNavigator";

class RootView extends Component {
  constructor(props){
    super(props);
  }
  componentDidMount(){
    BackHandler.addEventListener("hardwareBackPress", this.onBackPress);
    NetInfo.getConnectionInfo().then((connectionInfo) => {
      this.connectionChanged(connectionInfo);
    }).catch(err => ({}));
    NetInfo.addEventListener(
        'connectionChange',
        this.connectionChanged
    );
  }
  componentWillUnmount(){
    BackHandler.removeEventListener("hardwareBackPress", this.onBackPress);
    NetInfo.removeEventListener(
        'connectionChange',
        this.connectionChanged
    )
  }
  connectionChanged = (connectionInfo) => {
    this.props.connectionChanged(connectionInfo);
  };
  onBackPress = () => {
    const { dispatch, nav } = this.props;
    if (nav.index === 0) {
      return false;
    }
    dispatch(NavigationActions.back());
    return true;
  };
  render(){
    return (
        <Root>
          <AppNavigator navigation={addNavigationHelpers({
            dispatch: this.props.dispatch,
            state: this.props.nav
          })}/>
          {!this.props.network &&
          <View style={styles.networkIndicator}>
            <Text style={styles.networkIndicatorText}>No internet connection</Text>
          </View>}
        </Root>
    )
  }
}

const styles = StyleSheet.create({
  networkIndicator: {
    height:20,
    alignItems:'center',
    justifyContent:'center',
    backgroundColor: "#E84855",
    position:'absolute',
    width: '100%',
    bottom:0
  },
  networkIndicatorText: {
    color: "white"
  }
});

function mapDispatchToProps(dispatch) {
  const boundActionCreators = bindActionCreators(ActionCreators, dispatch);
  return {...boundActionCreators, dispatch};
}

export default connect(store => ({network: store.auth.network, nav: store.nav}), mapDispatchToProps)(RootView);
import React, {Component} from 'react';
import {Text} from 'native-base';
import {StyleSheet} from "react-native";

export class BoldText extends Component {
  setNativeProps = (nativeProps) => {
    this._root.setNativeProps(nativeProps);
  };
  render(){
    return (
        <Text ref={component => this._root = component} {...this.props} style={[styles.text, this.props.style]}>
          {this.props.children}
        </Text>
    )
  }
}

const styles = StyleSheet.create({
  text: {
    fontFamily: 'museo_Bold'
  }
});

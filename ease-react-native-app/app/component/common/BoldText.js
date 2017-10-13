import React, {Component} from 'react';
import {Text} from 'native-base';
import {StyleSheet} from "react-native";

class BoldText extends Component {
  render(){
    return (
        <Text style={[this.props.style, styles.text]}>
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

export default BoldText;
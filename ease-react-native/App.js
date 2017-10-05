import React from 'react';
import { StyleSheet, Text, View, TextInput, Button, Image } from 'react-native';

export default class App extends React.Component {
  render() {
    return (
      <View style={styles.container}>
        <Image
            style={{width: 100, height: 100}}
            source={{uri: 'https://ease.space/resources/images/ease.png'}}
        />
        <Text>Please login enculé !</Text>
        <TextInput editable={true} placeholder={'Login'} style={styles.input}/>
        <TextInput editable={true} placeholder={'Password'} style={styles.input}/>
        <Button title={'Log me'}/>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center',
    padding: 25
  },
  input: {
    width: '100%',
    height: 40
  }
});

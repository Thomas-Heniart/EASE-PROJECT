import {NavigationActions} from 'react-navigation';
import {Toast} from 'native-base';

export function resetNavigation(navigation, rootName){
  const resetAction = NavigationActions.reset({
    index: 0,
    actions: [
      NavigationActions.navigate({ routeName: rootName})
    ]
  });
  navigation.dispatch(resetAction);
}

export function alertToast(message, buttonText, duration){
  Toast.show({
    text: message,
    position: 'bottom',
    buttonText: buttonText,
    duration: duration,
    type:'danger'
  });
}
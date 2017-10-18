import { NavigationActions } from 'react-navigation';
import AppNavigator from "../component/AppNavigator";

const initialState = AppNavigator.router.getStateForAction(AppNavigator.router.getActionForPathAndParams('SplashScreen'));

const defaultGetStateForAction = AppNavigator.router.getStateForAction;

AppNavigator.router.getStateForAction = (action, state) => {
  if (
      state &&
      action.type === NavigationActions.BACK &&
      (
          state.routes[state.index].routeName === 'Login' ||
          state.routes[state.index].routeName === 'Home'
      )
  ) {
    return null;
  }
  return defaultGetStateForAction(action, state);
};


const navReducer = (state = initialState, action) => {
  let nextState;

  switch(action.type){
    case 'RESET_NAVIGATION': {
      const routeName = action.payload.routeName;
      nextState = AppNavigator.router.getStateForAction(AppNavigator.router.getActionForPathAndParams(routeName));
      break;
    }
    default : {
      nextState = AppNavigator.router.getStateForAction(action, state);
      break;
    }
  }
  return nextState || state;
};

export default navReducer;
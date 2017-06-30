const initialState = {
  TeamMultiAppAddActive: false,
  TeamSimpleAppAddActive: false,
  TeamLinkAppAddActive: false
};

export default function reducer(state=initialState, action) {
  switch (action.type){
    case 'SHOW_APP_ADD_UI_ELEMENT': {
      var state = {...initialState};
      state['Team' + action.payload.type + 'AppAddActive'] = action.payload.state;
      return state;
    }
    case 'CLOSE_APP_ADD_UI': {
      return {...initialState}
    }
  }
  return state;
}
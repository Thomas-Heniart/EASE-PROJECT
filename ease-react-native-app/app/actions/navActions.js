export function resetNavigation({routeName}) {
  return (dispatch, getState) => {
    dispatch({
      type: 'RESET_NAVIGATION',
      payload: {
        routeName: routeName
      }
    });
  }
}
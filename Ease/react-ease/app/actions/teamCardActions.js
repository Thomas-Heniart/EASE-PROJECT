export function createTeamCard({team_id, channel_id, website, type, name, url, subtype, account_information}) {
  return (dispatch) => {
    dispatch({
      type: 'ADD_TEAM_CARD', payload: {
        team_id: team_id,
        channel_id: channel_id,
        app: website,
        name: name,
        url: url,
        type: type,
        subtype: subtype,
        account_information: account_information
      }
    });
  }
}

export function resetTeamCard() {
  return (dispatch) => {
    dispatch({type: 'RESET_TEAM_CARD'});
  }
}

export function moveTeamCard({card_id}) {
  return (dispatch) => {
    dispatch({
      type: 'MOVE_TEAM_CARD', payload: {
        card_id: card_id
      }
    })
  }
}
import React from 'react';
import {connect} from "react-redux";
import SimpleTeamAppAdder from "./teamAppAdders/SimpleTeamAppAdder";
import LinkTeamAppAdder from "./teamAppAdders/LinkTeamAppAdder";
import EnterpriseTeamAppAdder from "./teamAppAdders/EnterpriseTeamAppAdder";
import SimpleTeamUpdateAppAdder from "./teamAppAdders/SimpleTeamUpdateAppAdder";

@connect(store =>({
  card: store.teamCard
}))
class TeamAppAddingUi extends React.Component {
  constructor(props){
    super(props);
  }
  render(){
    const item = this.props.item;
    const view = this.props.addAppView;
    return (
        <div className="add_actions_container" id="app_add_actions">
          {this.props.card.account_information !== -1 && view === 'Simple' &&
          <SimpleTeamUpdateAppAdder item={item} accountInformation={this.props.account_information}/>}
          {view === 'Simple' && this.props.account_information === -1 &&
          <SimpleTeamAppAdder item={item} />}
          {view === 'Link' &&
          <LinkTeamAppAdder item={item} dispatch={this.props.dispatch} />}
          {view === 'Multi' &&
          <EnterpriseTeamAppAdder item={item} />}
        </div>
    )
  }
}

module.exports = TeamAppAddingUi;
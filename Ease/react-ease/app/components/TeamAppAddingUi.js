var React = require('react');
import {connect} from "react-redux";
import SimpleTeamAppAdder from "./teamAppAdders/SimpleTeamAppAdder";
import LinkTeamAppAdder from "./teamAppAdders/LinkTeamAppAdder";
import EnterpriseTeamAppAdder from "./teamAppAdders/EnterpriseTeamAppAdder";

@connect()
class TeamAppAddingUi extends React.Component {
  constructor(props){
    super(props);
  }
  render(){
    const item = this.props.item;
    const view = this.props.addAppView;

    return (
        <div className="add_actions_container" id="app_add_actions">
          {view === 'Simple' &&
          <SimpleTeamAppAdder item={item}/>}
          {view === 'Link' &&
          <LinkTeamAppAdder item={item} dispatch={this.props.dispatch}/>}
          {view === 'Multi' &&
          <EnterpriseTeamAppAdder item={item}/>}
        </div>
    )
  }
}

module.exports = TeamAppAddingUi;
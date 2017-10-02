var React = require('react');
import SimpleTeamApp from "./teamAppAdders/SimpleTeamApp";
import TeamLinkApp from "./teamAppAdders/LinkTeamApp";
import  EnterpriseTeamApp from "./teamAppAdders/EnterpriseTeamApp";
import queryString from "query-string";
import {connect} from "react-redux";
import {withRouter} from "react-router-dom";

@connect((store)=>{
  return {
    selectedItem: store.selection,
    users: store.users.users,
    channels: store.channels.channels,
    me: store.users.me,
    team_id: store.team.id,
    plan_id: store.team.plan_id
  };
})
class TeamAppsContainer extends React.Component{
  constructor(props){
    super(props);
  }
  componentWillReceiveProps(nextProps) {
    if (nextProps !== this.props){
      if (nextProps.location.search !== this.props.location.search){
        const query = queryString.parse(nextProps.location.search);
        if (query.app_id !== undefined && query.app_id.length !== 0){
          console.log(`app_${query.app_id}`);
          const el = document.getElementById(`app_${query.app_id}`);
          if (el)
            el.scrollIntoView(true);
        }
      }
    }
  }
  componentDidMount(){
    const query = queryString.parse(this.props.location.search);

    if (query.app_id !== undefined && query.app_id.length !== 0){
      const el = document.getElementById(`app_${query.app_id}`);
      console.log(`app_${query.app_id}`);
      console.log(el);
      if (el)
        el.scrollIntoView(true);
    }
  }
  render() {
    return (
        <div class="apps_container">
          <div class="apps_scroller_div" id="team_apps_container">
            {this.props.selectedItem.apps.map(function(item){
                if (item.type === 'simple')
                  return (
                      <SimpleTeamApp
                          app={item}
                          users={this.props.users}
                          channels={this.props.channels}
                          me={this.props.me}
                          key={item.id}
                          plan_id={this.props.plan_id}
                          team_id={this.props.team_id}
                          dispatch={this.props.dispatch}/>
                  );
                if (item.type === 'link')
                  return (
                      <TeamLinkApp
                          app={item}
                          users={this.props.users}
                          channels={this.props.channels}
                          me={this.props.me}
                          key={item.id}
                          team_id={this.props.team_id}
                          dispatch={this.props.dispatch}/>
                  );
                if (item.type === 'multi')
                  return (
                      <EnterpriseTeamApp
                          app={item}
                          plan_id={this.props.plan_id}
                          users={this.props.users}
                          channels={this.props.channels}
                          me={this.props.me}
                          key={item.id}
                          team_id={this.props.team_id}
                          dispatch={this.props.dispatch}/>
                  );
              }, this)
            }
          </div>
        </div>
    )
  }
}

module.exports = withRouter(TeamAppsContainer);
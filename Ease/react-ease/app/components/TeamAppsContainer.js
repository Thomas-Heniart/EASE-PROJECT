var React = require('react');
var classnames = require('classnames');
var TeamSimpleApp = require('./teamApps/TeamSimpleApp');
var TeamLinkApp = require('./teamApps/TeamLinkApp');
var TeamMultiApp = require('./teamApps/TeamMultiApp');
import {connect} from "react-redux"

@connect((store)=>{
  return {
    selectedItem: store.selection,
    users: store.users.users,
    channels: store.channels.channels,
    me: store.users.me
  };
})
class TeamAppsContainer extends React.Component{
  constructor(props){
    super(props);
  }
  render() {
    return (
        <div class="apps_container">
          <div class="apps_scroller_div" id="team_apps_container">
            {this.props.selectedItem.apps.map(function(item){
                if (item.type === 'simple')
                  return (
                      <TeamSimpleApp
                          app={item}
                          users={this.props.users}
                          channels={this.props.channels}
                          me={this.props.me}
                          key={item.id}
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
                          dispatch={this.props.dispatch}/>
                  );
                if (item.type === 'multi')
                  return (
                      <TeamMultiApp
                          app={item}
                          users={this.props.users}
                          channels={this.props.channels}
                          me={this.props.me}
                          key={item.id}
                          dispatch={this.props.dispatch}/>
                  );
              }, this)
            }
          </div>
        </div>
    )
  }
}

module.exports = TeamAppsContainer;
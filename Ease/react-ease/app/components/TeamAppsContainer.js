var React = require('react');
import {Loader} from 'semantic-ui-react';
import SimpleTeamApp from "./teamAppAdders/SimpleTeamApp";
import TeamLinkApp from "./teamAppAdders/LinkTeamApp";
import EnterpriseTeamApp from "./teamAppAdders/EnterpriseTeamApp";
import queryString from "query-string";
import {fetchTeamAppList} from "../actions/teamActions";
import {connect} from "react-redux";
import {withRouter} from "react-router-dom";

@connect(store => ({
  team_apps: store.team_apps
}))
class TeamAppsContainer extends React.Component{
  constructor(props){
    super(props);
    this.state = {
      loading: true
    }
  }
  componentWillReceiveProps(nextProps){
    if (this.props !== nextProps){
      if (this.props.item.id !== nextProps.item.id){
        this.setState({loading: true});
        const {item, team} = nextProps;
        this.props.dispatch(fetchTeamAppList({
          team_id: team.id,
          ids: item.team_card_ids
        })).then(response => {
          this.setState({loading: false});
        }).catch(err => {
          this.setState({loading: false});
        })
      }
    }
  }
  componentDidMount(){
    const {item, team} = this.props;
    this.props.dispatch(fetchTeamAppList({
      team_id: team.id,
      ids: item.team_card_ids
    })).then(response => {
      this.setState({loading: false});
    }).catch(err => {
      this.setState({loading: false});
    })
  }
  componentDidUpdate(prevProps, prevState) {
    if (prevProps !== this.props || prevState !== this.state) {
      if ((!this.state.loading && prevState.loading !== this.state.loading) || this.props.location.search !== prevProps.location.search) {
        const query = queryString.parse(this.props.location.search);
        if (query.app_id !== undefined && query.app_id.length !== 0) {
          const el = document.getElementById(`app_${query.app_id}`);
          if (el) {
            el.scrollIntoView(true);
            el.classList.add('blink');
            window.setTimeout(() => {
              el.classList.remove('blink')
            }, 3000);
          }
        }
      }
    }
  }
  render() {
    const item = this.props.item;
    const team = this.props.team;
    const users = Object.keys(team.team_users).map(id => {
      return team.team_users[id];
    });
    const channels = Object.keys(team.rooms).map(id => {
      return team.rooms[id];
    });
    const me = team.team_users[team.my_team_user_id];
    const plan_id = team.plan_id;

      return (
        <div class="apps_container">
          <div class="apps_scroller_div" id="team_apps_container">
            {!this.state.loading ?
                item.team_card_ids.map(id => {
                    const item = this.props.team_apps[id];
                    if (item.type === 'teamSingleCard')
                    return (
                        <SimpleTeamApp
                            app={item}
                            users={users}
                            channels={channels}
                            me={me}
                            key={item.id}
                            plan_id={plan_id}
                            team_id={team.id}
                            dispatch={this.props.dispatch}/>
                    );
                  if (item.type === 'link')
                    return (
                        <TeamLinkApp
                            app={item}
                            users={users}
                            channels={channels}
                            me={me}
                            key={item.id}
                            team_id={team.id}
                            dispatch={this.props.dispatch}/>
                    );
                  if (item.type === 'multi')
                    return (
                        <EnterpriseTeamApp
                            app={item}
                            users={users}
                            channels={channels}
                            me={me}
                            key={item.id}
                            plan_id={plan_id}
                            team_id={team.id}
                            dispatch={this.props.dispatch}/>
                    );
                }) :
                <Loader active />
            }
          </div>
        </div>
    )
  }
}

module.exports = withRouter(TeamAppsContainer);
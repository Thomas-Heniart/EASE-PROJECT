var React = require('react');
import SimpleTeamApp from "./teamAppAdders/SimpleTeamApp";
import TeamLinkApp from "./teamAppAdders/LinkTeamApp";
import EnterpriseTeamApp from "./teamAppAdders/EnterpriseTeamApp";
import queryString from "query-string";
import {fetchTeamAppList} from "../actions/teamActions";
import {connect} from "react-redux";
import { Icon, Segment, Dimmer, Loader } from 'semantic-ui-react';
import {reflect} from "../utils/utils";
import {sendTeamUserInvitation} from "../actions/userActions";
import {withRouter} from "react-router-dom";

@connect(store => ({
  team_apps: store.team_apps,
  teams: store.teams
}))
class TeamAppsContainer extends React.Component{
  constructor(props){
    super(props);
    this.state = {
      loading: true,
      loadingSendInvitation: false
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
  sendInvitation = (teamUser) => {
    if (this.state.loadingSendInvitation !== true) {
      this.setState({loadingSendInvitation: true});
      const team = this.props.teams[this.props.match.params.teamId];
      this.props.dispatch(sendTeamUserInvitation({
        team_id: team.id,
        team_user_id: teamUser.id
      })).then(r => {
        setTimeout(() => {
          this.setState({loadingSendInvitation: false});
        }, 2000);
        // this.setState({loadingSendInvitation: false});
      }).catch(err => {
        setTimeout(() => {
          this.setState({loadingSendInvitation: false});
        }, 2000);
        // this.setState({loadingSendInvitation: false});
        // this.setState({errorMessage: err});
        console.log(err);
      });
      // this.setState({loadingSendInvitation: false});
    }
  };
  sendAllInvitations = () => {
    if (this.state.loadingSendInvitation !== true) {
      const team = this.props.teams[this.props.match.params.teamId];
      this.setState({loadingSendInvitation: true});
      const calls = Object.keys(team.team_users).map(item => {
        if (team.team_users[item].state === 0 && team.team_users[item].invitation_sent === false) {
          this.props.dispatch(sendTeamUserInvitation({
            team_id: team.id,
            team_user_id: Number(item)
          }));
        }
      });
      const response = calls.filter(item => {
        if (item)
          return true
      });
      Promise.all(response.map(reflect)).then(r => {
        setTimeout(() => {
          this.setState({loadingSendInvitation: false});
        }, 2000);
        // this.setState({loadingSendInvitation: false});
      }).catch(err => {
        setTimeout(() => {
          this.setState({loadingSendInvitation: false});
        }, 2000);
        // this.setState({loadingSendInvitation: false});
        // this.setState({errorMessage: err});
        console.log(err);
      });
    }
  };
  reSendAllInvitations = () => {
    if (this.state.loadingSendInvitation !== true) {
      const team = this.props.teams[this.props.match.params.teamId];
      this.setState({loadingSendInvitation: true});
      const calls = Object.keys(team.team_users).map(item => {
        if (team.team_users[item].state === 0 && team.team_users[item].invitation_sent === true) {
          this.props.dispatch(sendTeamUserInvitation({
            team_id: team.id,
            team_user_id: Number(item)
          }));
        }
      });
      const response = calls.filter(item => {
        if (item)
          return true
      });
      Promise.all(response.map(reflect)).then(r => {
        setTimeout(() => {
          this.setState({loadingSendInvitation: false});
        }, 2000);
        // this.setState({loadingSendInvitation: false});
      }).catch(err => {
        setTimeout(() => {
          this.setState({loadingSendInvitation: false});
        }, 2000);
        // this.setState({loadingSendInvitation: false});
        // this.setState({errorMessage: err});
        console.log(err);
      });
    }
  };
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
    let team_cards = [];
    if (!this.props.loading)
      team_cards = item.team_card_ids.map(id => {
        return this.props.team_apps[id];
      }).sort((a,b) => {
        return a.name.localeCompare(b.name);
      });
    return (
        <div class="apps_container">
          <div class="apps_scroller_div" id="team_apps_container">
            {item.state === 0 &&
            <div id='invitation'>
              {item.invitation_sent ?
                  <Segment className='resend' inverted disabled={this.state.loadingSendInvitation}>
                    {this.state.loadingSendInvitation ?
                      <div style={{textAlign: 'center'}}>
                        <span style={{textDecoration: 'none'}}>Invitations sent ! <Icon name='rocket'/></span>
                      </div>
                      :
                    <div>
                      {item.username} hasn’t joined your team yet. <span onClick={e => this.sendInvitation(item)}>Resend invitation <Icon name='send'/></span>
                      <Loader active={this.state.loadingSendInvitation} inverted size='tiny'/>
                      <span className='right' onClick={this.reSendAllInvitations}>Resend all pending invitations <Icon name='rocket'/></span>
                    </div>}
                  </Segment>
                  :
                  <Segment className='send' inverted disabled={this.state.loadingSendInvitation}>
                    <div>
                      {item.username} hasn’t been invited to join your team yet. <span onClick={e => this.sendInvitation(item)}>Send invitation <Icon name='send'/></span>
                      <Loader active={this.state.loadingSendInvitation} inverted size='tiny'/>
                      <span className='right' onClick={this.sendAllInvitations}>Send to all uninvited people <Icon name='rocket'/></span>
                    </div>
                  </Segment>}
            </div>}
            {!this.state.loading ?
                team_cards.map(item => {
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
                  if (item.type === 'teamLinkCard')
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
                  if (item.type === 'teamEnterpriseCard')
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
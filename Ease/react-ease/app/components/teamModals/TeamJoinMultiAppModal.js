var React = require('react');
var classnames = require('classnames');
var post_api = require('../../utils/post_api');
import {showTeamJoinMultiAppModal, showPinTeamAppToDashboardModal} from "../../actions/teamModalActions"
import * as appActions from "../../actions/appsActions"
import {findMeInReceivers} from "../../utils/helperFunctions"
import {connect} from "react-redux"

@connect((store)=>{
  return {
    modal: store.teamModals.teamJoinMultiAppModal
  };
})
class TeamJoinMultiAppModal extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      user: this.props.modal.user,
      app: this.props.modal.app,
      credentials: {}
    };
    Object.keys(this.state.app.website.information).map(function (item) {
      this.state.credentials[item] = '';
    }, this);
    this.isModalValid = this.isModalValid.bind(this);
    this.confirmModal = this.confirmModal.bind(this);
    this.handleInput = this.handleInput.bind(this);
  }
  handleInput(e){
    var credentials = {...this.state.credentials};
    credentials[e.target.name] = e.target.value;
    this.setState({credentials: credentials});
  }
  isModalValid(){
    const credentials = this.state.credentials;
    const keys = Object.keys(credentials);

    for (var i = 0; i < keys.length; i++){
      if (credentials[keys[i]].length === 0)
        return false;
    }
    return true;
  }
  confirmModal(){
    const app = this.state.app;
    const credentials = this.state.credentials;
    const user = this.state.user;

    this.props.dispatch(appActions.teamShareApp(app.id, {account_information: credentials, team_user_id: user.id})).then(response => {
      this.props.dispatch(appActions.teamAcceptSharedApp(app.id, response.shared_app_id)).then(response => {
        this.props.dispatch(showTeamJoinMultiAppModal(false));
        this.props.dispatch(showPinTeamAppToDashboardModal(true, {...app}));
      })
    });
  }
  render(){
    const app = this.state.app;
    const credentials = this.state.credentials;
    const webInfo = app.website.information;

    return (
        <div class="popupHandler myshow">
          <div class="popover_mask" onClick={e => {this.props.dispatch(showTeamJoinMultiAppModal(false))}}></div>
          <div class="ease_popup ease_team_popup" id="accept_multi_app_modal">
            <button class="button-unstyle action_button close_button" onClick={e => {this.props.dispatch(showTeamJoinMultiAppModal(false))}}>
              <i class="fa fa-times"/>
            </button>
            <div class="row title-row text-center">
              Enter your info, last time ever
            </div>
            <div class="row display-flex align_items_center" style={{paddingTop: '20px'}}>
              <div class="squared_image_handler">
                <img src={app.website.logo} alt="Website logo"/>
              </div>
              <span style={{fontSize: "1.3rem"}}>{app.name}</span>
            </div>
            <div class="row display-flex flex_direction_column" style={{padding: "20px 30px 30px 30px"}}>
              {
                Object.keys(credentials).reverse().map(function (item) {
                  return (
                      <div key={item} class="display-flex flex_direction_column input_handler">
                        <label htmlFor={item}>{webInfo[item].placeholder}</label>
                        <input type={webInfo[item].type}
                               name={item}
                               placeholder={webInfo[item].placeholder}
                               value={credentials[item]}
                               onChange={this.handleInput}
                               id={item}
                               class="modal_input input_unstyle"/>
                      </div>
                  )
                }, this)
              }
            </div>
            <button class="row button-unstyle positive_button big_validate_button"
                    disabled={!this.isModalValid()}
                    onClick={this.confirmModal}>
              CONFIRM
            </button>
          </div>
        </div>
    )
  }
}

module.exports = TeamJoinMultiAppModal;
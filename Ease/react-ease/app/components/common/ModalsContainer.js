import React, {Component} from "react";
import {connect} from "react-redux";
var TeamAddUserModal = require('../teamModals/TeamAddUserModal');
var TeamAddChannelModal = require('../teamModals/TeamAddChannelModal');
var TeamDeleteUserModal = require('../teamModals/TeamDeleteUserModal');
var TeamDeleteRoomModal = require('../teamModals/TeamDeleteRoomModal');
var TeamDeleteUserFromRoomModal = require('../teamModals/TeamDeleteUserFromRoomModal');
var PinTeamAppToDashboardModal = require('../teamModals/PinTeamAppToDashboardModal');
var TeamDeleteAppModal = require('../teamModals/TeamDeleteAppModal');
var TeamLeaveAppModal = require('../teamModals/TeamLeaveAppModal');
var TeamManageAppRequestModal = require('../teamModals/TeamManageAppRequestModal');
var TeamTransferOwnershipModal = require('../teamModals/TeamTransferOwnershipModal');
var TeamPhoneNumberModal = require('../teamModals/TeamPhoneNumberModal');
var RequestWebsiteModal = require('../teamModals/RequestWebsiteModal');
import AskJoinEnterpriseAppModal from "../teamModals/AskJoinEnterpriseAppModal";
import VerifyTeamUserModal from '../teamModals/VerifyTeamUserModal';
import ReactivateTeamUserModal from '../teamModals/ReactivateTeamUserModal';
import TeamAddMultipleUsersModal from '../teamModals/TeamAddMultipleUsersModal';
import JoinEnterpriseAppModal from "../teamModals/JoinEnterpriseAppModal";
import AcceptEnterpriseAppModal from "../teamModals/AcceptEnterpriseAppModal";
import EditEnterpriseAppModal from "../teamModals/EditEnterpriseAppModal";
import FreeTrialEndModal from "../teamModals/FreeTrialEndModal";
import UpgradeTeamPlanModal from "../teamModals/UpgradeTeamPlanModal";
import DepartureDateEndModal from "../teamModals/DepartureDateEndModal";

@connect(store => ({
  addUserModalActive: store.teamModals.addUserModalActive,
  addChannelModalActive: store.teamModals.addChannelModalActive,
  teamDeleteUserModal: store.teamModals.teamDeleteUserModal,
  teamDeleteChannelModal: store.teamModals.teamDeleteChannelModal,
  teamDeleteUserFromChannelModal: store.teamModals.teamDeleteUserFromChannelModal,
  teamDeleteAppModal: store.teamModals.teamDeleteAppModal,
  pinTeamAppToDashboardModal: store.teamModals.pinTeamAppToDashboardModal,
  teamLeaveAppModal: store.teamModals.teamLeaveAppModal,
  teamEditEnterpriseAppModal: store.teamModals.teamEditEnterpriseAppModal,
  teamManageAppRequestModal: store.teamModals.teamManageAppRequestModal,
  teamAcceptMultiAppModal: store.teamModals.teamAcceptMultiAppModal,
  teamJoinMultiAppModal: store.teamModals.teamJoinMultiAppModal,
  teamAskJoinEnterpriseAppModal: store.teamModals.teamAskJoinEnterpriseAppModal,
  teamSettingsModalActive: store.teamModals.teamSettingsModalActive,
  verifyTeamUserModal: store.teamModals.verifyTeamUserModal,
  teamAddMultipleUsersModal: store.teamModals.teamAddMultipleUsersModal,
  reactivateTeamUserModal: store.teamModals.reactivateTeamUserModal,
  teamTransferOwnershipModal: store.teamModals.teamTransferOwnershipModal,
  teamPhoneNumberModal: store.teamModals.teamPhoneNumberModal,
  requestWebsiteModal: store.teamModals.requestWebsiteModal,
  freeTrialEndModal: store.teamModals.freeTrialEndModal,
  upgradeTeamPlanModal: store.teamModals.upgradeTeamPlanModal,
  departureDateEndModal: store.teamModals.departureDateEndModal
}))
class ModalsContainer extends Component{
  constructor(props){
    super(props);
  }
  render() {
    return (
        <div>
          {this.props.addUserModalActive &&
          <TeamAddUserModal key="1"/>}
          {this.props.addChannelModalActive &&
          <TeamAddChannelModal/>}
          {this.props.teamDeleteUserModal.active &&
          <TeamDeleteUserModal/>}
          {this.props.teamDeleteChannelModal.active &&
          <TeamDeleteRoomModal/>}
          {this.props.teamDeleteUserFromChannelModal.active &&
          <TeamDeleteUserFromRoomModal/>}
          {this.props.teamDeleteAppModal.active &&
          <TeamDeleteAppModal/>}
          {this.props.pinTeamAppToDashboardModal.active &&
          <PinTeamAppToDashboardModal/>}
          {this.props.teamLeaveAppModal.active &&
          <TeamLeaveAppModal/>}
          {this.props.teamEditEnterpriseAppModal.active &&
          <EditEnterpriseAppModal/>}
          {this.props.teamManageAppRequestModal.active &&
          <TeamManageAppRequestModal/>}
          {this.props.teamAcceptMultiAppModal.active &&
          <AcceptEnterpriseAppModal/>}
          {this.props.teamJoinMultiAppModal.active &&
          <JoinEnterpriseAppModal/>}
          {this.props.teamAskJoinEnterpriseAppModal.active &&
          <AskJoinEnterpriseAppModal/>}
          {this.props.teamSettingsModalActive &&
          <TeamSettingsModal/>}
          {this.props.verifyTeamUserModal.active &&
          <VerifyTeamUserModal/>}
          {this.props.reactivateTeamUserModal.active &&
          <ReactivateTeamUserModal/>}
          {this.props.teamTransferOwnershipModal.active &&
          <TeamTransferOwnershipModal/>}
          {this.props.teamPhoneNumberModal.active &&
          <TeamPhoneNumberModal/>}
          {this.props.teamAddMultipleUsersModal.active &&
          <TeamAddMultipleUsersModal/>}
          {this.props.requestWebsiteModal.active &&
          <RequestWebsiteModal/>}
          {this.props.freeTrialEndModal.active &&
          <FreeTrialEndModal/>}
          {this.props.upgradeTeamPlanModal.active &&
          <UpgradeTeamPlanModal/>}
          {this.props.departureDateEndModal.active &&
          <DepartureDateEndModal/>}
        </div>
    )
  }
};

export default ModalsContainer;
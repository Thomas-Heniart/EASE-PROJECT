import React, {Component} from "react";
import {connect} from "react-redux";
import TeamAddUserModal from '../teamModals/TeamAddUserModal';
import TeamAddChannelModal from '../teamModals/TeamAddChannelModal';
import TeamDeleteUserModal from '../teamModals/TeamDeleteUserModal';
import TeamDeleteRoomModal from '../teamModals/TeamDeleteRoomModal';
import TeamDeleteUserFromRoomModal from '../teamModals/TeamDeleteUserFromRoomModal';
import PinTeamAppToDashboardModal from '../teamModals/PinTeamAppToDashboardModal';
import TeamDeleteAppModal from '../teamModals/TeamDeleteAppModal';
import TeamLeaveAppModal from '../teamModals/TeamLeaveAppModal';
import TeamManageAppRequestModal from '../teamModals/TeamManageAppRequestModal';
import TeamTransferOwnershipModal from '../teamModals/TeamTransferOwnershipModal';
import TeamPhoneNumberModal from '../teamModals/TeamPhoneNumberModal';
import RequestWebsiteModal from '../teamModals/RequestWebsiteModal';
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
import CatalogAddBookmarkModal from "../catalog/AddBookmarkModal";
import ClassicAppModal from "../catalog/ClassicAppModal";
import SsoAppModal from "../catalog/SsoAppModal";
import ClassicAppSettingsModal from "../modals/ClassicAppSettingsModal";
import ExtensionDownloadModal from "../modals/ExtensionDownloadModal";
import LinkAppSettingsModal from "../modals/LinkAppSettingsModal";
import PasswordLostInformationModal from "../modals/PasswordLostInformationModal";
import LockedTeamAppModal from "../modals/LockedTeamAppModal";
import LogWithAppSettings from "../modals/LogWithAppSettings";
import ChooseAppCredentialsModal from "../teamAppAdders/ChooseAppCredentialsModal";

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
  departureDateEndModal: store.teamModals.departureDateEndModal,
  catalogAddBookmarkModal: store.teamModals.catalogAddBookmarkModal,
  catalogAddAppModal: store.teamModals.catalogAddAppModal,
  catalogAddSSOAppModal: store.teamModals.catalogAddSSOAppModal,
  modals: store.modals
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
          {this.props.catalogAddBookmarkModal.active &&
          <CatalogAddBookmarkModal/>}
          {this.props.catalogAddAppModal.active &&
          <ClassicAppModal/>}
          {this.props.catalogAddSSOAppModal.active &&
          <SsoAppModal/>}
          {this.props.modals.classicAppSettings.active &&
          <ClassicAppSettingsModal/>}
          {this.props.modals.extensionDownload.active &&
          <ExtensionDownloadModal/>}
          {this.props.modals.linkAppSettings.active &&
          <LinkAppSettingsModal/>}
          {this.props.modals.passwordLostInformation.active &&
          <PasswordLostInformationModal/>}
          {this.props.modals.lockedTeamApp.active &&
          <LockedTeamAppModal/>}
          {this.props.modals.logWithAppSettings.active &&
          <LogWithAppSettings/>}
          {this.props.modals.chooseAppCredentials.active &&
          <ChooseAppCredentialsModal/>}
        </div>
    )
  }
};

export default ModalsContainer;